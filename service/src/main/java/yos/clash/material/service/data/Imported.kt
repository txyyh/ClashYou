package yos.clash.material.service.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import yos.clash.material.service.model.Profile
import java.util.*

@Entity(tableName = "imported", primaryKeys = ["uuid"])
@TypeConverters(Converters::class)
data class Imported(
    @ColumnInfo(name = "uuid") val uuid: UUID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: Profile.Type,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "interval") val interval: Long,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
)