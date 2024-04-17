package cn.merlin.bean

import kotlinx.serialization.Serializable

@Serializable
class Message (
    val messageId: Int = 0,
    val messageType: Int = 0,
    val messageContent: String = "test",
    val messageSenderIpAddress: String = "",
    val messageReceiverIpAddress: String = "",
    val messageSenderIdentifier: String = "",
    val messageReceiverIdentifier: String = ""
)