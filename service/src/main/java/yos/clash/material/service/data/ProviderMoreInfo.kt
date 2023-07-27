package yos.clash.material.service.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import java.util.*

@Entity(
    tableName = "provider_more_info",
    foreignKeys = [ForeignKey(
        entity = Imported::class,
        childColumns = ["uuid"],
        parentColumns = ["uuid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    primaryKeys = ["uuid"]
)
@TypeConverters(Converters::class)
data class ProviderMoreInfo(
    @ColumnInfo(name = "uuid") val uuid: UUID,
    @ColumnInfo(name = "upload") val upload: Long,
    @ColumnInfo(name = "download") val download: Long,
    @ColumnInfo(name = "total") val total: Long,
    @ColumnInfo(name = "expire") val expire: Long,
)