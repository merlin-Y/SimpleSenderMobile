package cn.nerlin.simplesendermobile.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.nerlin.simplesendermobile.bean.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("select * from device")
    fun getAllDevice(): Flow<Device>

    @Query("select * from device where deviceId = :deviceId")
    suspend fun getDeviceById(deviceId: Int): Device

    @Update
    suspend fun updateDevice(device: Device)

    @Insert
    suspend fun insert(device: Device)

    @Query("delete from device where deviceId = :deviceId")
    suspend fun delete(deviceId: Int)
}