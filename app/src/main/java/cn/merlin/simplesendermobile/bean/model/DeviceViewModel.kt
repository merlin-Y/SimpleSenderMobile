package cn.merlin.simplesendermobile.bean.model

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import cn.merlin.simplesendermobile.bean.Device

class DeviceViewModel(device: Device){
    val deviceId = mutableIntStateOf(device.deviceId)
    val deviceName = mutableStateOf(device.deviceName)
    val deviceIpAddress = mutableStateOf(device.deviceIpAddress)
    val deviceNickName = mutableStateOf(device.deviceNickName)
    val deviceIdentifier = mutableStateOf(device.deviceIdentifier)
    val deviceType = mutableStateOf(device.deviceType)
    val inListType = mutableStateOf(true)

    fun toDevice(): Device {
        return Device(
            deviceId.value,
            deviceName.value,
            deviceIpAddress.value,
            deviceNickName.value,
            deviceIdentifier.value,
            deviceType.value
        )
    }
}