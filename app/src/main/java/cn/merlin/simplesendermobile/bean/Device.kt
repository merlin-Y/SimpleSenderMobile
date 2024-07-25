package cn.merlin.simplesendermobile.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Device(
    @PrimaryKey(autoGenerate = true)
    var deviceId: Int = -1,
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
)