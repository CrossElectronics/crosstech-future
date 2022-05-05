package crosstech.future.logics.models

import crosstech.future.logics.Utils.Companion.computeSHA1
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import java.io.Serializable
import java.util.*

data class Task(
    var name: String,
    var description: String?,
    var creationTime: Date,
    var urgency: Urgency,
    var estDifficulty: Int,
    private var status: TaskStatus = TaskStatus.Planned,
    private var iconEnum: TaskIcon = TaskIcon.Planned
) : Serializable
{
    lateinit var scheduledTime: Date
    lateinit var completedTime: Date
    var deadline: Date? = null
    var reminder: Boolean = false
    var efficiency: Int = 0

    fun schedule(scheduledTime: Date, deadline: Date? = null, reminder: Boolean = false)
    {
        if (status != TaskStatus.Planned)
            throw IllegalStateException("Cannot schedule task from an unplanned state")
        status = TaskStatus.Scheduled
        this.scheduledTime = scheduledTime
        this.deadline = deadline
        this.reminder = reminder
    }

    fun complete(completedTime: Date, efficiency: Int)
    {
        if (status != TaskStatus.Scheduled || status != TaskStatus.Planned)
            throw IllegalStateException("Cannot complete unscheduled or unplanned state")
        status = TaskStatus.Completed
        this.completedTime = completedTime
        this.efficiency = efficiency
        reminder = false
    }

    fun unschedule()
    {
        if (status != TaskStatus.Scheduled)
            throw IllegalStateException("Cannot unschedule unscheduled task")
        status = TaskStatus.Planned
        deadline = null
        reminder = false
    }

    fun reopen()
    {
        if (status != TaskStatus.Completed)
            throw IllegalStateException("Cannot reopen uncompleted task")
        status = TaskStatus.Scheduled
    }

    fun getSHA1() = (name + description + creationTime).computeSHA1()
}