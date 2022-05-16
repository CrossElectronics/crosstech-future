package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.models.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArchivedTask(
    val name: String,
    val description: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val completeTime: LocalDateTime,
    val iconEnum: TaskIcon
) : Parcelable
{
    val sha1 = (name + description + creationTime).computeSHA1()

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readSerializable() as LocalDateTime,
        parcel.readSerializable() as LocalDateTime,
        parcel.readSerializable() as TaskIcon
    )

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeSerializable(creationTime)
        parcel.writeSerializable(completeTime)
        parcel.writeSerializable(iconEnum)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArchivedTask>
    {
        override fun createFromParcel(parcel: Parcel): ArchivedTask
        {
            return ArchivedTask(parcel)
        }

        override fun newArray(size: Int): Array<ArchivedTask?>
        {
            return arrayOfNulls(size)
        }
    }
}
