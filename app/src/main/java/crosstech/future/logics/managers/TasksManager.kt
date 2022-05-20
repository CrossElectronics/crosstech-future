package crosstech.future.logics.managers

import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.ArchivedTask
import crosstech.future.logics.models.Task
import java.time.LocalDate
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
            filtered.assignIcon(TaskIcon.Planned) { it.status == TaskStatus.Planned }
            filtered.assignIcon(TaskIcon.Scheduled) { it.status == TaskStatus.Scheduled }
            filtered.assignIcon(TaskIcon.Important) { it.isImportant }
            filtered.assignIcon(TaskIcon.Deadline) { it.isDeadlineApproaching() }
            // sort
            val sortedList = filtered.sortedWith(
                compareByDescending<Task> { it.getPriority() }
                    .thenBy {
                        ChronoUnit.MINUTES.between(
                            LocalDateTime.now(),
                            it.deadline ?: LocalDateTime.MAX
                        )
                    }
                    .thenBy { it.scheduledTime ?: LocalDateTime.MAX })
            return sortedList.toMutableList()
        }

        private fun List<Task>.assignIcon(icon: TaskIcon, predicate: (Task) -> Boolean)
        {
            val filtered = this.filter(predicate)
            for (item in filtered) item.iconEnum = icon
        }

        fun filterCompletedTaskAndSort(tasks: List<Task>): MutableList<Task>
        {
            return tasks
                .filter { it.status == TaskStatus.Completed }
                .sortedByDescending { it.completedTime }
                .toMutableList()
        }

        fun sortArchivedTask(tasks: List<ArchivedTask>): MutableList<ArchivedTask>
        {
            return tasks
                .sortedByDescending { it.completeTime }
                .toMutableList()
        }

        fun filterTodayTask(tasks: List<Task>): List<Task>
        {
            val today = LocalDate.now().atStartOfDay()
            return tasks
                .filter { it.scheduledTime != null && it.status != TaskStatus.Completed }
                .filter {
                    today.isAfter(it.scheduledTime!!) &&
                    today.isBefore(it.deadline ?: today.plusDays(1))
                    // if today is between scheduled time and deadline
                    // if deadline does not exist, make it today + 1
                }
                .sortedByDescending { it.scheduledTime }
        }
    }
}