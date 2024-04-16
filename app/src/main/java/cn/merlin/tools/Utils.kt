package cn.merlin.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.network.CurrentDeviceInformation

val currentDevice = mutableStateOf(DeviceModel(Device()))
val detectedDeviceIdentifierList: MutableList<String> = mutableListOf()

fun getUserProfile(): String{
    return ""
}