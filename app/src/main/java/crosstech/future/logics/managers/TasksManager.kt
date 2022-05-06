package crosstech.future.logics.managers

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
            val sortedList = filtered.sortedWith(
                compareByDescending<Task> { it.urgency == Urgency.Urgent }
                    .thenByDescending { it.isImportant }
                    .thenBy { ChronoUnit.MINUTES.between(LocalDateTime.now(), it.deadline) }
                    .thenBy { it.scheduledTime })
            return sortedList.toMutableList()
        }
    }
}