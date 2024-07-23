package cn.merlin.simplesendermobile.bean.model

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import cn.merlin.simplesendermobile.bean.Message

class MessageViewModel(message : Message) {
    val messageId = mutableIntStateOf(message.messageId)
    val messageType = mutableIntStateOf(message.messageType)
    val messageContent = mutableStateOf(message.messageContent)
    val messageSenderIpAddress = mutableStateOf(message.messageSenderIpAddress)
    val messageReceiverIpAddress = mutableStateOf(message.messageReceiverIpAddress)
    val messageSenderIdentifier = mutableStateOf(message.messageSenderIdentifier)
    val messageReceiverIdentifier = mutableStateOf(message.messageReceiverIdentifier)

    fun toMessage(): Message {
        return Message(
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