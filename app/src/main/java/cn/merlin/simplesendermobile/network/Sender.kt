package cn.merlin.simplesendermobile.network

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.bean.model.DeviceModel
import kotlinx.coroutines.*
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.math.ceil

val DetectedDevices: SnapshotStateList<DeviceModel> = mutableStateListOf()

class Sender() {

    private var currentDevice = Device()

    fun detectDevices() {
        currentDevice = CurrentDeviceInformation.getInformation()
        val ipAddressIndex = currentDevice.deviceIpAddress.substring(0, currentDevice.deviceIpAddress.lastIndexOf("."))
        println("Scanning from $ipAddressIndex.1~$ipAddressIndex.255")
        for (i in 1..255) {
            CoroutineScope(Dispatchers.IO).launch {
                val ipAddress = "$ipAddressIndex.$i"
                if (ipAddress == currentDevice.deviceIpAddress) cancel()
                try {
                    val socket = Socket()
//                    withContext(Dispatchers.IO) {
                    socket.connect(InetSocketAddress(ipAddress, 19999), 100)
                    val objectInputStream = ObjectInputStream(socket.getInputStream())
                    val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                    objectOutputStream.writeInt(1)
                    val device = objectInputStream.readObject() as Device
                    DetectedDevices.add(DeviceModel(device))
//                    }
                    socket.close()
                } catch (e: Exception) {
//                            print(e)
                }
            }
        }
        println("${currentDevice.deviceName} ${currentDevice.deviceIpAddress} ${currentDevice.deviceMacAddress}")
        println("Scanning accomplished")
    }

    fun sendFileToSelectedDevice(device: DeviceModel, file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val packetSize = 1024
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(device.deviceIpAddress.value,19999),100)
                val datagramSocket = DatagramSocket()
                val data = file.readBytes()
                val totalPackets = ceil(data.size.toDouble() / packetSize).toInt()
                val objectInputStream = ObjectInputStream(socket.getInputStream())
                val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                objectOutputStream.writeInt(2)
                val requestCode = objectInputStream.readInt()
                if(requestCode == 1){
                    objectOutputStream.writeObject(cn.merlin.simplesendermobile.bean.File(file.name,totalPackets))
                }
                else   this.cancel()
                for (i in 0 until totalPackets) {
                    val offset = i * packetSize
                    val length = if (offset + packetSize < data.size) packetSize else data.size - offset
                    val packetData = data.copyOfRange(offset, length)
                    launch(Dispatchers.IO) {
                        val packet = DatagramPacket(
                            packetData,
                            length,
                            InetAddress.getByName(device.deviceIpAddress.value),
                            20000
                        )
                        datagramSocket.send(packet)
                    }
                }
                datagramSocket.close()
            } catch (e: Exception) {
//
            }
        }
    }
}