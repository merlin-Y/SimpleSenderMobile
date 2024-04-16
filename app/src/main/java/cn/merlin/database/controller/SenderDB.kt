package cn.merlin.database.controller

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.merlin.database.dao.DeviceDao
import cn.merlin.database.dao.MessageDao
import cn.merlin.database.model.Device
import cn.merlin.database.model.Message

@Database(entities = [Device::class,Message::class],version = 1, exportSchema = false)
abstract class SenderDB: RoomDatabase() {
    abstract fun getDeviceDao(): DeviceDao
    abstract fun getMessageDao(): MessageDao

    companion object {
        @Volatile
        private var Instance: SenderDB? = null

        fun getDatabase(context: Context): SenderDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SenderDB::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}