package cn.merlin.database.dao

import androidx.room.*
import cn.merlin.database.model.Message
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