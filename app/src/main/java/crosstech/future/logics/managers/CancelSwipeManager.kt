package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.gui.tasks.OpenTaskFragment
import crosstech.future.logics.models.TaskListAdapter
import java.time.LocalDateTime

class CancelSwipeManager(
    val view: View,
    private val recyclerView: RecyclerView,
    private val global: Global,
    private val frag: OpenTaskFragment
) :
    SimpleCallback(0, LEFT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as TaskListAdapter
        val toRemove = adapter.retrieveData()[pos]
        val removed = toRemove.cancel(LocalDateTime.now())
        adapter removeAt pos
        global.tasks.remove(toRemove)
        global.archive.add(removed)
        frag.updateHeader(updateTasks = true, updateArchive = true)
        Snackbar.make(recyclerView, "Task cancelled: ${toRemove.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                global.tasks.add(toRemove)
                global.archive.remove(removed)
                val i = adapter differAndAddFrom TasksManager.filterOpenTasksAndSort(global.tasks)
                if (i != null) recyclerView.scrollToPosition(i)
                frag.updateHeader(updateTasks = true, updateArchive = true)
            }
            .show()
    }
}