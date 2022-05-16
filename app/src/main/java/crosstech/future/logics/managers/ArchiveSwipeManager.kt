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

class ArchiveSwipeManager(
    val view: View,
    private val recyclerView: RecyclerView,
    private val global: Global,
    private val frag: CompletedTaskFragment
) :
    SimpleCallback(0, RIGHT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as TaskListAdapter
        val toArchive = adapter.retrieveData()[pos]
        val archived = toArchive.archive()
        adapter removeAt pos
        global.tasks.remove(toArchive)
        global.archive.add(archived)
        frag.updateHeader(updateTask = true, updateArchive = true)
        Snackbar.make(recyclerView, "Task archived: ${toArchive.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                global.tasks.add(toArchive)
                global.archive.remove(archived)
                val i =
                    adapter differAndAddFrom TasksManager.filterCompletedTaskAndSort(global.tasks)
                if (i != null) recyclerView.scrollToPosition(i)
                frag.updateHeader(updateTask = true, updateArchive = true)
            }
            .show()
    }

}