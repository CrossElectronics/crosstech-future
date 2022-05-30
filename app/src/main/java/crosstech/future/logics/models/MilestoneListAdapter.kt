package crosstech.future.logics.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R
import crosstech.future.logics.Utils.Companion.toLocalDateTime
import java.time.Duration
import java.time.LocalDateTime

class MilestoneListAdapter(var data: MutableList<Milestone>,
                           private val editListener: (View, Milestone) -> Unit,
                           private val cardListener: (View, Milestone) -> Unit) :
    RecyclerView.Adapter<MilestoneViewHolder>()
{
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder
    {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.milestone_card, parent, false)
        context = view.context
        return MilestoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int)
    {
        val datum = data[position]
        with(holder) {
            title.text = datum.name
            description.text = datum.description

            if (datum.ongoingCommit != null)
            {
                val duration =
                    Duration.between(datum.ongoingCommit!!.toLocalDateTime(), LocalDateTime.now())
                val totalMinutes = duration.toMinutes()
                ongoingCommitTime.text =
                    String.format("%d:%02d", totalMinutes / 60, totalMinutes % 60)
            }
            else
            {
                ongoingCommitTime.text = context.getString(R.string.empty_commit_time)
            }
            commitCount.text = datum.getCommitCount().toString()
            commitHours.text = String.format("%.2f", datum.getCommitHours())
            if (datum.getCommitCount() == 0)
            {
                lastCommit.text = context.getString(R.string.no_commits)
            }
            else
            {
                datum.commits.sortByDescending { it.endTime }
                lastCommit.text = datum.commits.first().getSHA1().substring(0 .. 6)
            }

            editButton.setOnClickListener { editListener(it, datum) }
            view.setOnClickListener { cardListener(it, datum) }
        }
    }

    override fun getItemCount(): Int = data.size
}