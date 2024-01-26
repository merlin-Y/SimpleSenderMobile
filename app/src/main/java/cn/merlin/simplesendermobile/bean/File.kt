package cn.merlin.simplesendermobile.bean

import java.io.Serializable

data class File(
    var fileName: String = "",
    var totalPackets: Int = -1
): Serializable