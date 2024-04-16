package cn.merlin.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import cn.merlin.bean.Device
import com.zy.devicelibrary.UtilsApp
import com.zy.devicelibrary.utils.GeneralUtils
import com.zy.devicelibrary.utils.OtherUtils
import java.net.Inet4Address

object CurrentDeviceInformation {
    private val currentDevice = Device()

    private fun getDeviceInformation(){
        currentDevice.deviceName = OtherUtils.getDeviceName()
        currentDevice.deviceType = if(OtherUtils.isTabletDevice() == 0) "phone" else "laptop"
        val connectivityManager = UtilsApp.getApp().applicationContext.getSystemService<ConnectivityManager>()
        if (connectivityManager != null) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true
            ) {
                val linkProperties = connectivityManager.getLinkProperties(network)
                linkProperties?.linkAddresses?.forEach { linkAddress ->
                    val address = linkAddress.address
                    if (address is Inet4Address) {
                        currentDevice.deviceIpAddress = address.hostAddress!!
                        return@forEach
                    }
                }
            }
        }
    }

    fun isWifiConnect(): Boolean{
        return GeneralUtils.getNetworkType() == "NETWORK_WIFI"
    }

    fun getInformation(): Device{
        getDeviceInformation()
        return currentDevice
    }
}