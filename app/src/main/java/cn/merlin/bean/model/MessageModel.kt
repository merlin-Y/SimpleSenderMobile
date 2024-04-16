package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Message

class MessageModel(message: Message) {
    var messageId = mutableStateOf(message.messageId)
    var messageType = mutableStateOf(message.messageType)
    var messageContent = mutableStateOf(message.messageContent)
    var messageSenderIpAddress = mutableStateOf(message.messageSenderIpAddress)
    var messageReceiverIpAddress = mutableStateOf(message.messageReceiverIpAddress)
    var messageSenderIdentifier = mutableStateOf(message.messageSenderIdentifier)
    var messageReceiverIdentifier = mutableStateOf(message.messageReceiverIdentifier)

    fun toMessage(): cn.merlin.database.model.Message {
        return cn.merlin.database.model.Message(
            messageId.value,
            messageType.value,
            messageContent.value,
            messageSenderIpAddress.value,
            messageReceiverIpAddress.value,
            messageSenderIdentifier.value,
            messageReceiverIdentifier.value
        )
    }
}