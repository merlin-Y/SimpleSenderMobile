package cn.merlin.simplesendermobile.bean

import java.io.Serializable

class Request(
    val requestCode: Int = -1,
    val totalPackets: Int = -1,
    val requestBody: Device = Device()
): Serializable