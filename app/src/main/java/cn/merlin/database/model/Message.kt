package cn.merlin.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Message (
    @PrimaryKey(autoGenerate = true)
    val messageId: Int?,
    @ColumnInfo(name = "messageType")
    val messageType: Int,
    @ColumnInfo(name = "messageContent")
    val messageContent: String,
    @ColumnInfo(name = "messageSenderIpAddress")
    val messageSenderIpAddress: String,
    @ColumnInfo(name = "messageReceiverIpAddress")
    val messageReceiverIpAddress: String,
    @ColumnInfo(name = "messageSenderIdentifier")
    val messageSenderIdentifier: String,
    @ColumnInfo(name = "messageReceiverIdentifier")
    val messageReceiverIdentifier: String
)