package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import crosstech.future.logics.Utils.Companion.getTotalHours
import kotlinx.serialization.Serializable
import java.time.Period

@Serializable
data class Milestone(var name: String,
                     var description: String,
                     var commits: MutableList<Commit>) : Parcelable
{
    fun getCommitHours(): Double
    {
        return commits
            .filter { it.endTime is Long && it.commitMessage is String }
            .sumOf { getTotalHours(it.startTime, it.endTime!!) }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Commit.CREATOR)?.toMutableList() ?: mutableListOf())

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeTypedList(commits)
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
