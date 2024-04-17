package cn.merlin.network

import cn.merlin.bean.Device
import kotlinx.coroutines.*
import java.net.*

class Sender {
    private val sendRequestPort = 19999
    private var currentDevice = Device()

    fun startScanning(){
        CoroutineScope(Dispatchers.IO).launch {
            while(true){
                currentDevice = CurrentDeviceInformation.getInformation()
                val commandCode = "SimpleSender"
                val broadcastAddress = InetAddress.getByName(currentDevice.deviceIpAddress.substring(0,currentDevice.deviceIpAddress.lastIndexOf('.') + 1) + "255")
                val sendMessage = "$commandCode;${currentDevice.deviceIpAddress}"
                val sendMessageArray = ByteArray(sendMessage.length + 2)
                sendMessageArray[0] = (sendMessage.length shr 8).toByte()
                sendMessageArray[1] = sendMessage.length.toByte()
                System.arraycopy(sendMessage.toByteArray(Charsets.UTF_8),0,sendMessageArray,2,sendMessage.length)
                val socket = DatagramSocket()
                try{
                    socket.broadcast = true
                    val packet =
                        DatagramPacket(sendMessageArray, sendMessage.length + 2, broadcastAddress, sendRequestPort)
                    socket.send(packet)
                }catch (_: Exception){
                    socket.close()
                }
                socket.close()
                delay(2000)
            }
        }
    }
}