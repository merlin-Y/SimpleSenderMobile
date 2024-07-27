package cn.merlin.simplesendermobile.network

import android.content.Context
import android.util.Log
import cn.merlin.simplesendermobile.bean.Device
import cn.merlin.simplesendermobile.tools.detectedDeviceIdentifierSet
import cn.merlin.simplesendermobile.tools.detectedDeviceList
import cn.merlin.simplesendermobile.tools.getWifiAddress
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
    private val networkJobs: MutableList<Job> = mutableListOf()

    fun startNetworkControl() {
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
                            startJobs()
                            Log.e("startJobs","startJobs")
                        }
                    } else {
                        stopJobs()
                        Log.e("stopJobs","stopJobs")
                        detectedDeviceList.clear()
                        detectedDeviceIdentifierSet.clear()
                    }
                }catch (_: Exception){
                    Log.e("jobException","jobException")
                }
            }
        }
    }

    private suspend fun startJobs() {
        if (networkJobs.isEmpty() && currentDevice.deviceIpAddress != "") {
            val sendCodeJob =  senderMobile.sendCodeRequest(currentDevice)
            val receiveDetectCodeJob = receiver.startDetectCodeReceiver(currentDevice)
            val receiveDeviceCodeJob = receiver.startDeviceCodeReceiver()
            val receiveMessageCodeJob = receiver.startMessageCodeReceiver()

            networkJobs.addAll(
                listOf(
                    sendCodeJob,
                    receiveDetectCodeJob,
                    receiveDeviceCodeJob,
                    receiveMessageCodeJob
                )
            )

            networkJobs.forEach{
                it.join()
            }
            Log.e("jobsStart","jobsStart")
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