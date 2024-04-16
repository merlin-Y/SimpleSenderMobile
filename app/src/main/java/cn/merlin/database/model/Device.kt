package cn.merlin.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true)
    val deviceId: Int = -1,
    @ColumnInfo(name = "deviceName")
    var deviceName: String = "",
    @ColumnInfo(name = "deviceIpAddress")
    var deviceIpAddress: String = "",
    @ColumnInfo(name = "deviceNickName")
    var deviceNickName: String = "",
    @ColumnInfo(name = "deviceIdentifier")
    var deviceIdentifier: String = "",
    @ColumnInfo(name = "deviceType")
    var deviceType: String = ""
){
    fun toDevice(): cn.merlin.bean.Device{
        return cn.merlin.bean.Device(
            deviceId,deviceName,deviceIpAddress,deviceNickName,deviceIdentifier,deviceType
        )
    }
}
