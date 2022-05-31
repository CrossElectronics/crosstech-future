package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import crosstech.future.logics.Utils
import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.Utils.Companion.toLocalDateTime
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Commit(val startTime: Long, val endTime: Long, val commitMessage: String) : Parcelable
{
    fun getSHA1(): String =
        (startTime.toString() + endTime.toString() + commitMessage).computeSHA1()

    fun getDuration(): Double = Utils.getTotalHours(startTime, endTime)

    fun getStartTime(): LocalDateTime = startTime.toLocalDateTime()

    fun getEndTime(): LocalDateTime = endTime.toLocalDateTime()

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeLong(startTime)
        parcel.writeValue(endTime)
        parcel.writeString(commitMessage)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Commit>
    {
        override fun createFromParcel(parcel: Parcel): Commit
        {
            return Commit(parcel)
        }

        override fun newArray(size: Int): Array<Commit?>
        {
            return arrayOfNulls(size)
        }
    }

}
