package cn.merlin.network

import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.bean.model.FileModel
import cn.merlin.tools.getUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Receiver {
    private val receiveCommandCodePort = 19999
    private val receiveRequestPort = 20000
    private val receivedPort = mutableSetOf(20000)
    private var currentDevice = Device()

    fun startServer(detectedDeviceList: SnapshotStateList<DeviceModel>) {
        currentDevice = CurrentDeviceInformation.getInformation()
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val receiverSocket = DatagramSocket(receiveCommandCodePort)
                val receiverBuffer = ByteArray(64)
                val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
                try {
                    receiverSocket.receive(receiverPacket)
                    receiverSocket.close()
                    val length = (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receiverPacket.data,2,buffer,0,length)
                    val cod = buffer.toString(Charsets.UTF_8)
                    if(cod.contains("SimpleSender")){
                        try{
                            val deviceJson = Json.encodeToString(currentDevice)
                            val ipAddress = cod.substring(cod.lastIndexOf(';') + 1, cod.length)
                            if(ipAddress != currentDevice.deviceIpAddress) {
                                val sendMessageArray = ByteArray(deviceJson.length + 12)
                                sendMessageArray[0] = ((deviceJson.length + 10) shr 8).toByte()
                                sendMessageArray[1] = (deviceJson.length + 10).toByte()
                                System.arraycopy("ipaddress;".toByteArray(), 0, sendMessageArray, 2, 10)
                                System.arraycopy(deviceJson.toByteArray(), 0, sendMessageArray, 12, deviceJson.length)
                                val socket = DatagramSocket()
                                val packet = DatagramPacket(
                                    sendMessageArray,
                                    deviceJson.length + 12,
                                    InetAddress.getByName(ipAddress),
                                    20000
                                )
                                socket.send(packet)
                                socket.close()
                            }
                        }catch (_: Exception){ }
                    }
                } catch (_: Exception) {
                    receiverSocket.close()
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val receiverSocket = DatagramSocket(receiveRequestPort)
                val receiverBuffer = ByteArray(512)
                val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
                try {
                    receiverSocket.receive(receiverPacket)
                    receiverSocket.close()
                    val length = (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receiverPacket.data,2,buffer,0,length)
                    val cod = buffer.toString(Charsets.UTF_8)
                    if(cod.contains("ipaddress")){
                        val device = Json.decodeFromString<Device>(cod.substring(cod.lastIndexOf(';') + 1,cod.length))
                        detectedDeviceList.add(DeviceModel(device))
                    }
                } catch (_: Exception) {
                    receiverSocket.close()
                }
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