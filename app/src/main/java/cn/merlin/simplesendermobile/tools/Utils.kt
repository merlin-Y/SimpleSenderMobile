package cn.merlin.simplesendermobile.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.datastore.DSManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

val currentDevice = mutableStateOf(DeviceViewModel(Device(deviceName = "我的设备")))
val isWifiConnected = mutableStateOf(false)
val savedDeviceList: MutableList<DeviceViewModel> = mutableListOf()
val savedDeviceIdentifierSet: MutableSet<String> = mutableSetOf()
val detectedDeviceList: MutableList<DeviceViewModel> = mutableListOf()
val detectedDeviceIdentifierSet: MutableSet<String> = mutableSetOf()
val isFrashing = mutableStateOf(true)

fun getUserProfile(): String {
    return ""
}

suspend fun isWifiConnected(context: Context): Boolean = withContext(Dispatchers.IO) {
    try {
        val connectivityManager =
            context.getSystemService("connectivity") as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        isWifiConnected.value = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        return@withContext networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    } catch (_: Exception) {
        return@withContext false
    }
}

suspend fun getWifiAddress(context: Context): Boolean = withContext(Dispatchers.IO) {
    try {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        currentDevice.value.deviceIpAddress.value = InetAddress.getByAddress(
            byteArrayOf(
                (ipAddress and 0xff).toByte(),
                (ipAddress shr 8 and 0xff).toByte(),
                (ipAddress shr 16 and 0xff).toByte(),
                (ipAddress shr 24 and 0xff).toByte()
            )
        ).hostAddress?.toString() ?: ""
        return@withContext true
    } catch (_: Exception) {
        return@withContext false
    }

}

suspend fun <U> updateSettings(dsManager: DSManager, key: String, value: U): U {
    CoroutineScope(Dispatchers.IO).launch{ dsManager.saveData(key, value) }
    return value
}

fun getDeviceName(context: Context){
    currentDevice.value.deviceName.value =
        Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME) ?: "我的设备"
}