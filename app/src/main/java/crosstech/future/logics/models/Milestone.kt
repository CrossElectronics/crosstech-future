package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import crosstech.future.logics.Utils.Companion.getTotalHours
import kotlinx.serialization.Serializable

@Serializable
data class Milestone(var name: String,
                     var description: String,
                     var commits: MutableList<Commit>,
                     var ongoingCommit: Long?) : Parcelable
{
    fun getCommitHours(): Double = commits.sumOf { getTotalHours(it.startTime, it.endTime) }
    fun getCommitCount(): Int = commits.size

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Commit.CREATOR)?.toMutableList() ?: mutableListOf(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeTypedList(commits)
        parcel.writeValue(ongoingCommit)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Milestone>
    {
        override fun createFromParcel(parcel: Parcel): Milestone
        {
            return Milestone(parcel)
        }

        override fun newArray(size: Int): Array<Milestone?>
        {
            return arrayOfNulls(size)
        }
    }
}
