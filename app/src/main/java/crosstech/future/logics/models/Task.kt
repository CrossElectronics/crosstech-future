package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.serializers.LocalDateTimeSerializer
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import java.time.temporal.ChronoUnit

@Serializable
data class Task(
    var name: String,
    var description: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    var creationTime: LocalDateTime,
    var urgency: Urgency,
    var isImportant: Boolean,
    var status: TaskStatus = TaskStatus.Planned,
    var iconEnum: TaskIcon = TaskIcon.Planned
) : Parcelable
{
    init
    {
        if (isImportant) iconEnum = TaskIcon.Important
    }

    @Serializable(with = LocalDateTimeSerializer::class)
    var scheduledTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeSerializer::class)
    var completedTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeSerializer::class)
    var deadline: LocalDateTime? = null
    var reminder: Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readSerializable() as LocalDateTime,
        parcel.readSerializable() as Urgency,
        parcel.readByte() != 0.toByte(),
        parcel.readSerializable() as TaskStatus,
        parcel.readSerializable() as TaskIcon
    )
    {
        scheduledTime = parcel.readSerializable() as LocalDateTime?
        completedTime = parcel.readSerializable() as LocalDateTime?
        deadline = parcel.readSerializable() as LocalDateTime?
        reminder = parcel.readByte() != 0.toByte()
    }

    /**
     * Creates a default empty task
     */
    constructor() : this(
        name = "",
        description = "",
        creationTime = LocalDateTime.now(),
        urgency = Urgency.Normal,
        isImportant = false
    )

    /**
     * Gets the tag (status summary) of current task
     */
    fun getTag(): String
    {
        // TODO: Localisation
        val result = when
        {
            isDeadlineApproaching()                   -> "Deadline approaching"
            urgency == Urgency.Urgent && !isImportant -> "Urgent"
            urgency != Urgency.Urgent && isImportant  -> "Important"
            urgency == Urgency.Urgent && isImportant  -> "Urgent important"
            status == TaskStatus.Scheduled            -> "Scheduled"
            else                                      -> "Planned"
        }
        return result
    }

    /**
     * Gets the priority of current task, higher means more important
     */
    fun getPriority(): Int
    {
        var priority = 0
        priority += when (urgency)
        {
            Urgency.Casual -> 0
            Urgency.Normal -> 2
            Urgency.Urgent -> 4
        }
        priority += if (isImportant) 2 else 0
        priority += if (scheduledTime != null) 1 else 0
        priority += when
        {
            deadline == null        -> 0
            isDeadlineApproaching() -> 2
            else                    -> 1
        }
        return priority
    }

    fun isDeadlineApproaching() =
        if (deadline == null) false
        else
            ChronoUnit.HOURS.between(LocalDateTime.now(), deadline) <= 24

    /**
     * Schedules this planned task
     * @param scheduledTime Time at which this task should start
     * @param deadline Optional, Time at which this task should be finished
     * @param reminder Optional, defaulted to false. If the app should remind you when deadline
     *                 is approaching
     * @throws IllegalStateException if status is not planned
     */
    fun schedule(
        scheduledTime: LocalDateTime,
        deadline: LocalDateTime? = null,
        reminder: Boolean = false
    ): Task
    {
        if (status != TaskStatus.Planned)
            throw IllegalStateException("Cannot schedule task from an unplanned state")
        status = TaskStatus.Scheduled
        this.scheduledTime = scheduledTime
        this.deadline = deadline
        this.reminder = reminder
        iconEnum = TaskIcon.Scheduled
        return this
    }

    /**
     * Completes this planned or scheduled task
     * @param completedTime Time at which the task is completed
     * @throws IllegalStateException if status is not planned or scheduled
     * @throws IllegalArgumentException if efficiency is out of range
     */
    fun complete(completedTime: LocalDateTime): Task
    {
        if (status != TaskStatus.Scheduled && status != TaskStatus.Planned)
            throw IllegalStateException("Cannot complete this state ($status)")
        status = TaskStatus.Completed
        this.completedTime = completedTime
        reminder = false
        iconEnum = TaskIcon.Completed
        return this
    }

    /**
     * Unschedules already scheduled task back into planned state
     * @throws IllegalStateException if status is not scheduled
     */
    fun unschedule(): Task
    {
        if (status != TaskStatus.Scheduled)
            throw IllegalStateException("Cannot unschedule unscheduled task")
        status = TaskStatus.Planned
        deadline = null
        reminder = false
        scheduledTime = null
        iconEnum = TaskIcon.Planned
        return this
    }

    /**
     * Reopens already completed task back into planned state
     * @throws IllegalStateException if status is not completed
     */
    fun reopenAnew(): Task
    {
        if (status != TaskStatus.Completed)
            throw IllegalStateException("Cannot reopen uncompleted task")
        status = TaskStatus.Planned
        deadline = null
        scheduledTime = null
        iconEnum = TaskIcon.Planned
        completedTime = null
        return this
    }

    fun reopen(status: TaskStatus, icon: TaskIcon): Task
    {
        this.status = status
        this.iconEnum = icon
        completedTime = null
        return this
    }

    fun archive(): ArchivedTask =
        ArchivedTask(
            name,
            description,
            creationTime,
            completedTime!!,
            TaskIcon.Completed
        )

    fun cancel(time: LocalDateTime): ArchivedTask =
        ArchivedTask(
            name,
            description,
            creationTime,
            time,
            TaskIcon.Cancelled
        )

    /**
     * Calculates message digestion of current task regardless of further status change
     * unless the name, description or the creation time is changed
     * @return SHA1 of this task in HEX, lowercase
     */
    fun getSHA1() = (name + description + creationTime).computeSHA1()

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (name != other.name) return false
        if (description != other.description) return false
        if (creationTime != other.creationTime) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + creationTime.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeSerializable(creationTime)
        parcel.writeSerializable(urgency)
        parcel.writeByte(if (isImportant) 1 else 0)
        parcel.writeSerializable(status)
        parcel.writeSerializable(iconEnum)
        parcel.writeSerializable(scheduledTime)
        parcel.writeSerializable(completedTime)
        parcel.writeSerializable(deadline)
        parcel.writeByte(if (reminder) 1 else 0)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task>
    {
        override fun createFromParcel(parcel: Parcel): Task
        {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?>
        {
            return arrayOfNulls(size)
        }
    }
}