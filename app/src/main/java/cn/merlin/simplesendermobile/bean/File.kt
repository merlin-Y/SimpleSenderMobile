package cn.merlin.simplesendermobile.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class File(
    @PrimaryKey(autoGenerate = true)
    var fileId: Int = -1,
    @ColumnInfo(name = "fileName")
    var fileName: String = "",
    @ColumnInfo(name = "filePath")
    var filePath: String = "",
    var dataSize: Int = -1,
    var totalPackets: Int = -1
)