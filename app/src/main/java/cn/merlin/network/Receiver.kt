package cn.merlin.network

import cn.merlin.bean.model.FileModel
import cn.merlin.tools.getUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.ServerSocket

class Receiver {
    private val receiveRequestPort = 19999
    private val receivedPort = mutableSetOf(19999)

    fun startServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverSocket = ServerSocket(receiveRequestPort)
                println("Server is listening on port $receiveRequestPort")
                while (true) {
                    val socket = serverSocket.accept()
                    println("New connection established")
                    val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                    val objectInputStream = ObjectInputStream(socket.getInputStream())
                    val request = objectInputStream.readInt()
                    if (request == 1) {
                        objectOutputStream.writeObject(CurrentDeviceInformation.getInformation())
                        objectOutputStream.flush()
                    } else if (request == 2) {
                        objectOutputStream.writeInt(1)
                        objectOutputStream.flush()
                        val receiveFile = objectInputStream.readObject() as FileModel
                        val port = getFreePort()
                        objectOutputStream.writeInt(port)
                        objectOutputStream.flush()
                        receiveFile(receiveFile, port)
                    }
                    socket.close()
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun receiveFile(receivedFile: FileModel, port: Int) {
        val packetSize = 10240
        var packetNumber = 0
        try {
            val socket = DatagramSocket(port)
            val receiveData = ByteArray(receivedFile.dataSize)
            val receivedPackets = mutableSetOf<Int>()
            while (receivedPackets.size < receivedFile.totalPackets) {
                val buffer = ByteArray(packetSize + 4)
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val offset = ((packet.data[0].toInt() shl 8) or (packet.data[1].toInt() and 0xFF)) * packetSize
                val length = (packet.data[2].toInt() shl 8) or (packet.data[3].toInt() and 0xFF)
                System.arraycopy(packet.data, 3, receiveData, offset, length)
                receivedPackets.add(packetNumber)
                packetNumber++
            }
            File(getUserProfile() +  receivedFile.fileName).writeBytes(receiveData)
            socket.close()
            receivedPort.remove(port)
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun getFreePort(): Int {
        var port = 20000
        while (receivedPort.contains(port)) {
            port += 1
        }
        receivedPort.add(port)
        return port
    }

}