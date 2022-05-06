package crosstech.future.logics.managers

import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.Task
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class TasksManager
{
    companion object
    {
        fun filterOpenTasksAndSort(tasks: List<Task>): MutableList<Task>
        {
            val filtered =
                tasks.filter { it.status == TaskStatus.Planned || it.status == TaskStatus.Scheduled }
            // adjust icons
            val important = tasks.filter { it.isImportant }
            for (item in important) item.iconEnum = TaskIcon.Important
            val deadlineApproaching =
                filtered.filter { it.deadline != null }
                    .filter { ChronoUnit.HOURS.between(LocalDateTime.now(), it.deadline) <= 24 }
            for (item in deadlineApproaching) item.iconEnum = TaskIcon.Deadline
            // sort
            val sortedList = filtered.sortedWith(
                compareByDescending<Task> { it.urgency == Urgency.Urgent }
                    .thenByDescending { it.isImportant }
                    .thenBy { ChronoUnit.MINUTES.between(LocalDateTime.now(), it.deadline) }
                    .thenBy { it.scheduledTime })
            return sortedList.toMutableList()
        }
    }
}