package cn.merlin.simplesendermobile.bean

import java.io.Serializable

data class Device(
    var deviceName: String = "",
    var deviceIpAddress: String = "",
    var deviceMacAddress: String = "",
    var deviceNickName: String = ""
): Serializable