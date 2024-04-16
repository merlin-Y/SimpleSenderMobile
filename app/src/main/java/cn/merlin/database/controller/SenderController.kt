package cn.merlin.database.controller

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.bean.model.MessageModel
import cn.merlin.database.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class SenderController(context: Context) {
    private val senderDB = SenderDB.getDatabase(context)

    fun getAllDevice(localDeviceList: SnapshotStateList<DeviceModel>){
        CoroutineScope(Dispatchers.IO).launch {
            val list = senderDB.getDeviceDao().getAllDevice()
            list.forEach {
                localDeviceList.add(DeviceModel(it.toDevice()))
            }
        }
    }

    fun getAllMessage(): Flow<Message>{
        var messageList: Flow<Message> = flowOf()
        CoroutineScope(Dispatchers.IO).launch {
            messageList = senderDB.getMessageDao().getAllMessage()
        }
        return messageList
    }

    fun insertDevice(deviceModel: DeviceModel){
        CoroutineScope(Dispatchers.IO).launch{
            senderDB.getDeviceDao().insert(deviceModel.toDevice())
        }
    }

    fun insertMessage(messageModel: MessageModel){
        CoroutineScope(Dispatchers.IO).launch {
            senderDB.getMessageDao().insertMessage(messageModel.toMessage())
        }
    }

    fun updateDevice(deviceModel: DeviceModel){
        CoroutineScope(Dispatchers.IO).launch {
            senderDB.getDeviceDao().updateDevice(deviceModel.toDevice())
        }
    }

    fun updateMessage(messageModel: MessageModel){
        CoroutineScope(Dispatchers.IO).launch {
            senderDB.getMessageDao().updateMessage(messageModel.toMessage())
        }
    }

    fun deleteDevice(deviceId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            senderDB.getDeviceDao().delete(deviceId)
        }
    }

    fun deleteMessage(messageId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            senderDB.getMessageDao().deleteMessageById(messageId)
        }
    }
}