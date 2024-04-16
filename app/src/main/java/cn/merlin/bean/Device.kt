package cn.merlin.bean

import cn.merlin.database.model.Device
import java.io.Serializable

data class Device(
    var deviceId: Int = -1,
    var deviceName: String = "",
    var deviceIpAddress: String = "",
    var deviceNickName: String = "",
    var deviceIdentifier: String = "",
    var deviceType: String = ""
): Serializable