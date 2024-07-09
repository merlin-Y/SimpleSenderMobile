package cn.nerlin.simplesendermobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.nerlin.simplesendermobile.bean.Device
import cn.nerlin.simplesendermobile.bean.Message
import cn.nerlin.simplesendermobile.database.dao.DeviceDao
import cn.nerlin.simplesendermobile.database.dao.MessageDao

@Database(entities = [Device::class, Message::class],version = 1, exportSchema = false)
abstract class ssDB: RoomDatabase() {
    abstract fun getDeviceDao(): DeviceDao
    abstract fun getMessageDao(): MessageDao

    companion object {
        @Volatile
        private var Instance: ssDB? = null

        fun getDatabase(context: Context): ssDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ssDB::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}