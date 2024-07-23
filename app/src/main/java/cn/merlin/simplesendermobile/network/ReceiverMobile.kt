package cn.merlin.simplesendermobile.network

import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.tools.getUserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ReceiverMobile {
    private val receiveCommandCodePort = 19999
    private val receiveDeviceCodePort = 20000
    private val receiveMessageCodePort = 20001
    private val receivedPort: MutableSet<Int> = mutableSetOf()

    fun startDetectCodeReceiver(currentDevice: Device): Job{
        return CoroutineScope(Dispatchers.IO).launch{
            while (true) {
                val receiveSocket = DatagramSocket(receiveCommandCodePort)
                val receiveBuffer = ByteArray(64)
                val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
                try {
                    receiveSocket.receive(receivePacket)
                    receiveSocket.close()
                    val length = (receivePacket.data[0].toInt() shl 8) or (receivePacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receivePacket.data, 2, buffer, 0, length)
                    val cnb = buffer.toString(Charsets.UTF_8)
                    if (cnb.contains("detectCode")) {
                        try {
                            val deviceJson = Json.encodeToString(currentDevice)
                            val ipAddress = cnb.substring(cnb.lastIndexOf(';') + 1, cnb.length)
                            if (ipAddress != currentDevice.deviceIpAddress) {
                                val sendMessageArray = ByteArray(deviceJson.length + 16)
                                sendMessageArray[0] = ((deviceJson.length + 14) shr 8).toByte()
                                sendMessageArray[1] = (deviceJson.length + 14).toByte()
                                System.arraycopy("receiveDevice;".toByteArray(Charsets.UTF_8) + deviceJson.toByteArray(), 0, sendMessageArray, 2, sendMessageArray.size)
                                val socket = DatagramSocket()
                                val packet = DatagramPacket(
                                    sendMessageArray,
                                    deviceJson.length + 12,
                                    InetAddress.getByName(ipAddress),
                                    receiveDeviceCodePort
                                )
                                socket.send(packet)
                                socket.close()
                            }
                        } catch (_: Exception) { }
                    }
                } catch (_: Exception) {
                    receiveSocket.close()
                }
            }
        }
    }

    fun startDeviceCodeReceiver(detectedDeviceList: SnapshotStateList<DeviceViewModel>): Job{
        return CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val receiverSocket = DatagramSocket(receiveDeviceCodePort)
                val receiverBuffer = ByteArray(512)
                val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
                try {
                    receiverSocket.receive(receiverPacket)
                    receiverSocket.close()
                    val length = (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receiverPacket.data,2,buffer,0,length)
                    val cnb = buffer.toString(Charsets.UTF_8)
                    if(cnb.contains("receiveDevice")){
                        val device = Json.decodeFromString<Device>(cnb.substring(cnb.lastIndexOf(';') + 1,cnb.length))
                        detectedDeviceList.add(DeviceViewModel(device))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    receiverSocket.close()
                }
            }
        }
    }

    fun startMessageCodeReceiver(): Job{
        return CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val receiverSocket = DatagramSocket(receiveMessageCodePort)
                val receiverBuffer = ByteArray(512)
                val receiverPacket = DatagramPacket(receiverBuffer, receiverBuffer.size)
                try {
                    receiverSocket.receive(receiverPacket)
                    receiverSocket.close()
                    val length = (receiverPacket.data[0].toInt() shl 8) or (receiverPacket.data[1].toInt() and 0xFF)
                    val buffer = ByteArray(length)
                    System.arraycopy(receiverPacket.data,2,buffer,0,length)
                    val cnb = buffer.toString(Charsets.UTF_8)
                    if(cnb.contains("receiveFile")){

//                        receiveFile()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    receiverSocket.close()
                }
            }
        }
    }

    private fun receiveFile(receivedFile: cn.merlin.simplesendermobile.bean.File, port: Int) {
        val packetSize = 10240
        var packetNumber = 0
        val socket = DatagramSocket(port)
        try {
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
            File(getUserProfile() + receivedFile.fileName).writeBytes(receiveData)
            socket.close()
            receivedPort.remove(port)
        } catch (e: Exception) {
            socket.close()
            receivedPort.remove(port)
            println(e)
        }
    }

    private fun getFreePort(): Int {
        var port = 20002
        while (receivedPort.contains(port)) {
            port += 1
        }
        receivedPort.add(port)
        return port
    }
}