package cn.merlin.simplesendermobile.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.tools.isWifiConnected
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkController(private val context: Context) {
    private val senderMobile = SenderMobile()
    private val receiver = ReceiverMobile()
    private val currentDevice = Device()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var wifiStateReceiver: BroadcastReceiver? = null
    private var networkJobs = mutableListOf<Job>()

    fun startNetworkControl(
        detectedDeviceList: SnapshotStateList<DeviceViewModel>
    ) {
        coroutineScope.launch {
            while (true) {
                delay(2000)
                isWifiConnected(context)
                val wifiState = isWifiConnected(context)
                if (wifiState) {
                    val ipAddress = getWifiAddress()
                    cn.merlin.simplesendermobile.tools.currentDevice.value.deviceIpAddress.value = ipAddress
                    if (currentDevice.deviceIpAddress != ipAddress) {
                        currentDevice.deviceIpAddress = ipAddress
                        println("IP Address Updated: ${currentDevice.deviceIpAddress}")
                    }
                    startJobs(detectedDeviceList)
                } else {
                    stopJobs()
                }
            }
        }
//        CoroutineScope(Dispatchers.Default).launch {
//            val sendCodeJob = senderMobile.sendCodeRequest(currentDevice)
//            val receiveDetectCodeJob = receiver.startDetectCodeReceiver(currentDevice)
//            val receiveDeviceCodeJob = receiver.startDeviceCodeReceiver(detectedDeviceList)
//            val receiveMessageCodeJob = receiver.startMessageCodeReceiver()
//            val getIpAddressJob = CoroutineScope(Dispatchers.IO).launch {
//                currentDevice.deviceIpAddress = getWifiAddress()
//                if (cn.merlin.simplesendermobile.tools.currentDevice.value.deviceIpAddress.value != currentDevice.deviceIpAddress) {
//                    currentDevice.deviceIpAddress =
//                        cn.merlin.simplesendermobile.tools.currentDevice.value.deviceIpAddress.value
//                }
//                delay(2000)
//            }
//
//            try{
//                while (true) {
//                    if (detectedWifiState()) {
//                        getIpAddressJob.start()
//                    } else {
//                        getIpAddressJob.cancel()
//                    }
//                    if (detectedWifiState() && currentDevice.deviceIpAddress != "") {
//                        sendCodeJob.start()
//                        receiveDetectCodeJob.start()
//                        receiveDeviceCodeJob.start()
//                        receiveMessageCodeJob.start()
//                    } else {
//                        sendCodeJob.cancel()
//                        receiveDetectCodeJob.cancel()
//                        receiveDeviceCodeJob.cancel()
//                        receiveMessageCodeJob.cancel()
//                    }
//                    delay(2000)
//                }
//            }catch (_: Exception){}
//        }
    }

    private fun startJobs(detectedDeviceList: SnapshotStateList<DeviceViewModel>) {
        if (networkJobs.isEmpty() && currentDevice.deviceIpAddress != "") {
            val sendCodeJob = coroutineScope.launch { senderMobile.sendCodeRequest(currentDevice) }
            val receiveDetectCodeJob = coroutineScope.launch { receiver.startDetectCodeReceiver(currentDevice) }
            val receiveDeviceCodeJob = coroutineScope.launch { receiver.startDeviceCodeReceiver(detectedDeviceList) }
            val receiveMessageCodeJob = coroutineScope.launch { receiver.startMessageCodeReceiver() }

            networkJobs.addAll(listOf(sendCodeJob, receiveDetectCodeJob, receiveDeviceCodeJob, receiveMessageCodeJob))
        }
    }

    private fun stopJobs() {
        networkJobs.forEach { it.cancel() }
        networkJobs.clear()
    }

    private suspend fun getWifiAddress(): String {
        return cn.merlin.simplesendermobile.tools.getWifiAddress(context).toString()
    }

    fun stopNetworkControl() {
        context.unregisterReceiver(wifiStateReceiver)
        stopJobs()
    }
}