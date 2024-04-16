package cn.merlin.database.dao

import androidx.room.*
import cn.merlin.database.model.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("select * from device")
    fun getAllDevice(): MutableList<Device>

    @Query("select * from device where deviceId = :deviceId")
    suspend fun getDeviceById(deviceId: Int): Device

    @Update
    suspend fun updateDevice(device: Device)

    @Insert
    suspend fun insert(device: Device)

    @Query("delete from device where deviceId = :deviceId")
    suspend fun delete(deviceId: Int)
}