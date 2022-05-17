package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Milestone(var name: String,
                     var openTasks: MutableList<Task>,
                     var archive: MutableList<ArchivedTask>) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(Task.CREATOR)?.toMutableList() ?: mutableListOf(),
        parcel.createTypedArrayList(ArchivedTask.CREATOR)?.toMutableList() ?: mutableListOf())

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeTypedList(openTasks)
        parcel.writeTypedList(archive)
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
