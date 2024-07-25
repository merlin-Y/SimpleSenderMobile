package cn.merlin.simplesendermobile.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.tools.detectedDeviceIdentifierSet
import cn.merlin.simplesendermobile.tools.getWifiAddress
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

class NetworkController(private val context: Context, private val networkJobs: MutableList<Job>) {
    private val senderMobile = SenderMobile()
    private val receiver = ReceiverMobile()
    private val currentDevice = Device()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun startNetworkControl(
        detectedDeviceList: SnapshotStateList<DeviceViewModel>
    ) {
        coroutineScope.launch {
            while (true) {
                delay(2000)
                try{
                    if (getWifiAddress(context)) {
                        currentDevice.deviceIpAddress =
                            cn.merlin.simplesendermobile.tools.currentDevice.value.deviceIpAddress.value
                        currentDevice.deviceName =
                            cn.merlin.simplesendermobile.tools.currentDevice.value.deviceName.value
                        currentDevice.deviceIdentifier =
                            cn.merlin.simplesendermobile.tools.currentDevice.value.deviceIdentifier.value
                        if (networkJobs.isEmpty() && currentDevice.deviceIpAddress != "") {
                            startJobs(detectedDeviceList)
                        }
                    } else {
                        stopJobs()
                        detectedDeviceList.clear()
                        detectedDeviceIdentifierSet.clear()
                    }
                }catch (_: Exception){}
            }
        }
    }

    private fun startJobs(detectedDeviceList: SnapshotStateList<DeviceViewModel>) {
        if (networkJobs.isEmpty() && currentDevice.deviceIpAddress != "") {
            val sendCodeJob = coroutineScope.launch { senderMobile.sendCodeRequest(currentDevice) }
            val receiveDetectCodeJob =
                coroutineScope.launch { receiver.startDetectCodeReceiver(currentDevice) }
            val receiveDeviceCodeJob =
                coroutineScope.launch { receiver.startDeviceCodeReceiver(detectedDeviceList) }
            val receiveMessageCodeJob =
                coroutineScope.launch { receiver.startMessageCodeReceiver() }

            networkJobs.addAll(
                listOf(
                    sendCodeJob,
                    receiveDetectCodeJob,
                    receiveDeviceCodeJob,
                    receiveMessageCodeJob
                )
            )
        }
    }

    private fun stopJobs() {
        networkJobs.forEach { it.cancel() }
        networkJobs.clear()
    }

    fun stopNetworkControl() {
        stopJobs()
    }
}