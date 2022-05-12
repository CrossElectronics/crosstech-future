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
import java.time.LocalDateTime

class RightSwipeManager(
    val view: View,
    private val recyclerView: RecyclerView,
    private val global: Global
) :
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
        // TODO: remove efficiency and difficulty from task
        removed.complete(LocalDateTime.now(), 5)
        adapter removeAt pos
        Snackbar.make(recyclerView, "Task completed: ${removed.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                removed.reopen(origStatus, origIcon)
                val i = adapter differAndAddFrom TasksManager.filterOpenTasksAndSort(global.tasks)
                if (i != null) recyclerView.scrollToPosition(i)
            }
            .show()
    }

}