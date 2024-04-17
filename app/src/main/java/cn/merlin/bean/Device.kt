package cn.merlin.bean

import kotlinx.serialization.Serializable


@Serializable
data class Device(
    var deviceId: Int = -1,
    var deviceName: String = "",
    var deviceIpAddress: String = "",
    var deviceNickName: String = "",
    var deviceIdentifier: String = "",
    var deviceType: String = ""
)