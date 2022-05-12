package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.gui.OpenTaskFragment
import crosstech.future.logics.models.Task
import crosstech.future.logics.models.TaskListAdapter

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
        val removed = adapter.retrieveData()[pos]
        adapter removeAt pos
        global.tasks.remove(removed)
        frag.updateHeader()
        Snackbar.make(recyclerView, "Task cancelled: ${removed.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                val list = adapter.retrieveData()
                list.add(removed)
                val i = adapter differAndAddFrom TasksManager.filterOpenTasksAndSort(list)
                if (i != null) recyclerView.scrollToPosition(i)
                global.tasks.add(removed)
                frag.updateHeader()
            }
            .show()
    }

}