package cn.merlin.network

import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.tools.detectedDeviceIdentifierList
import kotlinx.coroutines.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class Sender {
    private var currentDevice = Device()

    fun detectDevice(detectedDeviceList: SnapshotStateList<DeviceModel>){
        CoroutineScope(Dispatchers.IO).launch {
            while (true){
                currentDevice = CurrentDeviceInformation.getInformation()
                val ipAddressIndex =
                    currentDevice.deviceIpAddress!!.substring(0, currentDevice.deviceIpAddress!!.lastIndexOf("."))
                for (i in 1..255) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val ipAddress = "$ipAddressIndex.$i"
                        if (ipAddress == currentDevice.deviceIpAddress) cancel()
                        try {
                            val socket = Socket()
                            socket.connect(InetSocketAddress(ipAddress, 19999), 1000)
                            val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
                            val objectInputStream = ObjectInputStream(socket.getInputStream())
                            objectOutputStream.writeInt(1)
                            objectOutputStream.flush()
                            val device = objectInputStream.readObject() as Device
                            if(device.deviceIpAddress != currentDevice.deviceIpAddress){
                                if(!detectedDeviceIdentifierList.contains(device.deviceIdentifier)){
                                    detectedDeviceList.add(DeviceModel(device))
                                }
                            }
                            socket.close()
                        } catch (e: Exception) {
//                            print(e)
                        }
                    }
                }
                delay(2000)
            }
        }
    }
}