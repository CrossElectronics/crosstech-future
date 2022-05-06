package crosstech.future.logics.models

import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.serializers.LocalDateTimeAsStringSerializer
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import kotlinx.serialization.Serializable

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
)
{
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var scheduledTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var completedTime: LocalDateTime? = null

    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    var deadline: LocalDateTime? = null
    var reminder: Boolean = false
    var efficiency: Int = 0

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
}