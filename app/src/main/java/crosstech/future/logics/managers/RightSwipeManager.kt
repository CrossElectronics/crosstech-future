package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.models.Task
import crosstech.future.logics.models.TaskListAdapter

class RightSwipeManager(val view: View, val recyclerView: RecyclerView, val global: Global) :
    SimpleCallback(0, RIGHT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as TaskListAdapter
        val removed = adapter.retrieveData()[pos]
        val origStatus = removed.status
        val origIcon = removed.iconEnum
        removed.status = TaskStatus.Completed
        removed.iconEnum = TaskIcon.Completed
        adapter removeAt pos
        Snackbar.make(recyclerView, "Task completed: ${removed.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                removed.status = origStatus
                removed.iconEnum = origIcon
                val i = adapter differAndAddFrom TasksManager.filterOpenTasksAndSort(global.tasks)
                if (i != null) recyclerView.scrollToPosition(i)
            }
            .show()
    }

}