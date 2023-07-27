package yos.clash.material.service.data

import androidx.room.*
import java.util.*

@Dao
@TypeConverters(Converters::class)
interface ProviderMoreInfoDao {
    @Query("SELECT * FROM provider_more_info WHERE uuid = :uuid")
    suspend fun queryByUUID(uuid: UUID): ProviderMoreInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setInfo(info: ProviderMoreInfo)
}