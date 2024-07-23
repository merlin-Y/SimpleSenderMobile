package cn.merlin.simplesendermobile.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message (
    @PrimaryKey(autoGenerate = true)
    var messageId: Int,
    @ColumnInfo(name = "messageType")
    var messageType: Int,
    @ColumnInfo(name = "messageContent")
    var messageContent: String,
    @ColumnInfo(name = "messageSenderIpAddress")
    var messageSenderIpAddress: String,
    @ColumnInfo(name = "messageReceiverIpAddress")
    var messageReceiverIpAddress: String,
    @ColumnInfo(name = "messageSenderIdentifier")
    var messageSenderIdentifier: String,
    @ColumnInfo(name = "messageReceiverIdentifier")
    var messageReceiverIdentifier: String
)