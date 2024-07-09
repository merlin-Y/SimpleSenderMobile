package cn.nerlin.simplesendermobile.bean.beanviewers

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import cn.nerlin.simplesendermobile.bean.Device

class DeviceViewer(device: Device){
    var deviceId = mutableIntStateOf(device.deviceId)
    var deviceName = mutableStateOf(device.deviceName)
    var deviceIpAddress = mutableStateOf(device.deviceIpAddress)
    var deviceNickName = mutableStateOf(device.deviceNickName)
    var deviceIdentifier = mutableStateOf(device.deviceIdentifier)
    var deviceType = mutableStateOf(device.deviceType)
    var inListType = mutableStateOf(true)

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