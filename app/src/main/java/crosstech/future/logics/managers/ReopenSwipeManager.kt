package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.gui.CompletedTaskFragment
import crosstech.future.logics.models.TaskListAdapter
import java.time.LocalDateTime

class ReopenSwipeManager(
    val view: View,
    private val recyclerView: RecyclerView,
    private val global: Global,
    private val frag: CompletedTaskFragment
) :
    SimpleCallback(0, LEFT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as TaskListAdapter
        val target = adapter.retrieveData()[pos]
        val time = target.completedTime
        target.reopenAnew()
        adapter removeAt pos
        frag.updateHeader(true)
        Snackbar.make(recyclerView, "Task reopened: ${target.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                target.complete(time ?: LocalDateTime.now(), 5)
                val i =
                    adapter differAndAddFrom TasksManager.filterCompletedTaskAndSort(global.tasks)
                if (i != null) recyclerView.scrollToPosition(i)
                frag.updateHeader(true)
            }
            .show()
    }

}