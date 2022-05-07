package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.logics.models.Task
import crosstech.future.logics.models.TaskListAdapter

class LeftSwipeManager(val view: View, val recyclerView: RecyclerView, val global: Global) :
    SimpleCallback(0, LEFT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as TaskListAdapter
        val removed = adapter.retrieveData()[pos]
        adapter removeAt pos
        global.tasks = adapter.retrieveData()
        Snackbar.make(recyclerView, "Task cancelled: ${removed.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                val list = adapter.retrieveData()
                list.add(removed)
                adapter differAndAddFrom TasksManager.filterOpenTasksAndSort(list)
                global.tasks = adapter.retrieveData()
            }
            .show()
    }

}