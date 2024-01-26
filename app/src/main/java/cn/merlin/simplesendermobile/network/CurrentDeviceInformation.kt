package cn.merlin.simplesendermobile.network

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import cn.merlin.simplesendermobile.bean.Device

object CurrentDeviceInformation {
    private val currentDevice = Device()

    fun getDeviceInformation(context: Context) {
        currentDevice.deviceName = Build.MODEL
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val intIpAddress: Int = wifiInfo.ipAddress
        val ipAddress = String.format(
            "%d.%d.%d.%d",
            intIpAddress and 0xff,
            intIpAddress shr 8 and 0xff,
            intIpAddress shr 16 and 0xff,
            intIpAddress shr 24 and 0xff
        )
        currentDevice.deviceIpAddress = ipAddress
    }

    fun getInformation(): Device {
        return currentDevice
    }
}