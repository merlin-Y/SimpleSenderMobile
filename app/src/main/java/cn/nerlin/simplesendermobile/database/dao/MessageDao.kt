package cn.nerlin.simplesendermobile.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.nerlin.simplesendermobile.bean.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("select * from message")
    fun getAllMessage(): Flow<Message>

    @Query("select * from message where messageId = :messageId")
    suspend fun getMessageById(messageId: Int): Message

    @Insert
    suspend fun insertMessage(message: Message)

    @Update
    suspend fun updateMessage(message: Message)

    @Query("delete from message where messageId = :messageId")
    suspend fun deleteMessageById(messageId: Int)
}