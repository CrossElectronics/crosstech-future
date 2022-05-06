package crosstech.future.logics.models

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.serializers.LocalDateTimeAsStringSerializer
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.temporal.ChronoUnit
import kotlin.concurrent.timer
import kotlin.math.log

@Serializable
data class Task(
    var name: String,
    var description: String?,
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var creationTime: LocalDateTime,
    var urgency: Urgency,
    var isImportant: Boolean,
    var estDifficulty: Int,
    var status: TaskStatus = TaskStatus.Planned,
    var iconEnum: TaskIcon = TaskIcon.Planned
) : Parcelable
{
    init
    {
        if (isImportant) iconEnum = TaskIcon.Important
    }

    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var scheduledTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var completedTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var deadline: LocalDateTime? = null
    var reminder: Boolean = false
    var efficiency: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readSerializable() as LocalDateTime,
        parcel.readSerializable() as Urgency,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readSerializable() as TaskStatus,
        parcel.readSerializable() as TaskIcon
    )
    {
        scheduledTime = parcel.readSerializable() as LocalDateTime?
        completedTime = parcel.readSerializable() as LocalDateTime?
        deadline = parcel.readSerializable() as LocalDateTime?
        reminder = parcel.readByte() != 0.toByte()
        efficiency = parcel.readInt()
    }

    /**
     * Creates a default empty task
     */
    constructor() : this(
        name = "",
        description = "",
        creationTime = LocalDateTime.now(),
        urgency = Urgency.Normal,
        isImportant = false,
        estDifficulty = 5
    )

    /**
     * Gets the tag (status summary) of current task
     */
    fun getTag(): String
    {
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
     * @param efficiency Efficiency grade in range 1..5
     * @throws IllegalStateException if status is not planned or scheduled
     * @throws IllegalArgumentException if efficiency is out of range
     */
    fun complete(completedTime: LocalDateTime, efficiency: Int): Task
    {
        if (status != TaskStatus.Scheduled || status != TaskStatus.Planned)
            throw IllegalStateException("Cannot complete unscheduled or unplanned state")
        if (efficiency !in 1 .. 5)
            throw IllegalArgumentException("Efficiency must be in range of 1..5")
        status = TaskStatus.Completed
        this.completedTime = completedTime
        this.efficiency = efficiency
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
    fun reopen(): Task
    {
        if (status != TaskStatus.Completed)
            throw IllegalStateException("Cannot reopen uncompleted task")
        status = TaskStatus.Planned
        deadline = null
        scheduledTime = null
        iconEnum = TaskIcon.Planned
        return this
    }

    fun archive(): ArchivedTask =
        if (status == TaskStatus.Completed) ArchivedTask(
            name,
            description,
            creationTime,
            completedTime!!,
            getSHA1()
        )
        else throw IllegalStateException("Cannot archive uncompleted task")

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
        parcel.writeInt(estDifficulty)
        parcel.writeSerializable(status)
        parcel.writeSerializable(iconEnum)
        parcel.writeSerializable(scheduledTime)
        parcel.writeSerializable(completedTime)
        parcel.writeSerializable(deadline)
        parcel.writeByte(if (reminder) 1 else 0)
        parcel.writeInt(efficiency)
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