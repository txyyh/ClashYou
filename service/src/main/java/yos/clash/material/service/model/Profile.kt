@file:UseSerializers(UUIDSerializer::class)

package yos.clash.material.service.model

import android.os.Parcel
import android.os.Parcelable
import com.github.kr328.clash.core.util.Parcelizer
import yos.clash.material.service.util.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class Profile(
    val uuid: UUID,
    val name: String,
    val type: Type,
    val source: String,
    val active: Boolean,
    val interval: Long,

    val updatedAt: Long,
    val imported: Boolean,
    val pending: Boolean,

    val moreInfo: MoreInfo?
) : Parcelable {
    enum class Type {
        File, Url, External
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        Parcelizer.encodeToParcel(serializer(), parcel, this)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {
        override fun createFromParcel(parcel: Parcel): Profile {
            return Parcelizer.decodeFromParcel(serializer(), parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }
}


@Serializable
data class MoreInfo(
    val uploadBase: Long,
    val downloadBase: Long,
    val totalBase: Long,
    val expireTime: Long,
)