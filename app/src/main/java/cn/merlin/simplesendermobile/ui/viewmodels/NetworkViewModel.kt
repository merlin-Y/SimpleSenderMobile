package cn.merlin.simplesendermobile.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import cn.merlin.simplesendermobile.bean.model.DeviceViewModel
import cn.merlin.simplesendermobile.network.NetworkController
import cn.merlin.simplesendermobile.tools.detectedDeviceList
import cn.merlin.simplesendermobile.tools.isFrashing
import cn.merlin.simplesendermobile.tools.savedDeviceList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkViewModel(application: Application): AndroidViewModel(application) {
    private val networkController = NetworkController(application)


    fun startNetwork(){
        networkController.startNetworkControl()
    }
    fun updateList(savedDeviceListView: SnapshotStateList<DeviceViewModel>, detectedDeviceListView: SnapshotStateList<DeviceViewModel>){
        CoroutineScope(Dispatchers.IO).launch{
            while(isFrashing.value){
                delay(500)
                savedDeviceListView.clear()
                savedDeviceListView.addAll(savedDeviceList)
                detectedDeviceListView.clear()
                detectedDeviceListView.addAll(detectedDeviceList)
            }
        }
    }
}