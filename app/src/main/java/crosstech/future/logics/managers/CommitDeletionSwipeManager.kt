package crosstech.future.logics.managers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import crosstech.future.Global
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.models.CommitListAdapter
import crosstech.future.logics.models.Milestone

class CommitDeletionSwipeManager(
    private val recyclerView: RecyclerView,
    private val milestone: Milestone,
    private val global: Global
) : ItemTouchHelper.SimpleCallback(0, LEFT)
{
    override fun onMove(r: RecyclerView, v: ViewHolder, t: ViewHolder) = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int)
    {
        val pos = viewHolder.adapterPosition
        val adapter = recyclerView.adapter as CommitListAdapter
        val toRemove = milestone.commits[pos]
        milestone.commits.remove(toRemove)
        adapter.notifyItemRemoved(pos)
        global.milestones.saveData(Global.MILESTONES_FILE, global.context)
        Snackbar.make(recyclerView,
                      "Commit removed: ${toRemove.commitMessage}",
                      Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                milestone.commits.add(toRemove)
                milestone.commits.sortByDescending { it.endTime }
                val index = milestone.commits.indexOf(toRemove)
                adapter.notifyItemInserted(index)
                recyclerView.scrollToPosition(index)
                global.milestones.saveData(Global.MILESTONES_FILE, global.context)
            }
            .show()
    }
}