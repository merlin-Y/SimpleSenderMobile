package cn.nerlin.simplesendermobile.bean.beanviewers

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import cn.nerlin.simplesendermobile.bean.Message

class MessageViewer(message : Message) {
    var messageId = mutableIntStateOf(message.messageId)
    var messageType = mutableIntStateOf(message.messageType)
    var messageContent = mutableStateOf(message.messageContent)
    var messageSenderIpAddress = mutableStateOf(message.messageSenderIpAddress)
    var messageReceiverIpAddress = mutableStateOf(message.messageReceiverIpAddress)
    var messageSenderIdentifier = mutableStateOf(message.messageSenderIdentifier)
    var messageReceiverIdentifier = mutableStateOf(message.messageReceiverIdentifier)

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