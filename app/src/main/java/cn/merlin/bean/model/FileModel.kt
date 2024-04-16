package cn.merlin.bean.model

import java.io.Serializable

data class FileModel(
    var fileName: String = "",
    var dataSize: Int = -1,
    var totalPackets: Int = -1
): Serializable