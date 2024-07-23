package cn.merlin.simplesendermobile.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.simplesendermobile.bean.File

class fileViewModel(file: File) {
    val fileId = mutableStateOf(file.fileId)
    val fileName = mutableStateOf(file.fileName)
    val filePath = mutableStateOf(file.filePath)

    fun toFile(): File{
        return File(fileId.value,fileName.value,filePath.value)
    }
}