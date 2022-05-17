package crosstech.future.logics.managers

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.gui.tasks.ArchivedFragment
import crosstech.future.logics.models.ArchivedListAdapter

class DeleteSwipeManager(
    val view: View,
    private val recyclerView: RecyclerView,
    private val global: Global,
    private val frag: ArchivedFragment
) :
    SimpleCallback(0, RIGHT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as ArchivedListAdapter
        val toDelete = adapter.retrieveData()[pos]
        adapter removeAt pos
        global.archive.remove(toDelete)
        frag.updateHeader(true)
        Snackbar.make(recyclerView, "Task archived: ${toDelete.name}", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                global.archive.add(toDelete)
                val i =
                    adapter differAndAddFrom TasksManager.sortArchivedTask(global.archive)
                if (i != null) recyclerView.scrollToPosition(i)
                frag.updateHeader(true)
            }
            .show()
    }

}