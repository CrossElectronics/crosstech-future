package crosstech.future.logics.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R
import crosstech.future.logics.enums.TaskStatus
import java.time.ZoneOffset

class MilestoneListAdapter(var data: MutableList<Milestone>,
                           private val listener: (View, Milestone) -> Unit) :
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
            openTask.text =
                datum.tasks.count { it.status == TaskStatus.Planned || it.status == TaskStatus.Scheduled }
                    .toString()
            closedTask.text = datum.tasks.count { it.status == TaskStatus.Completed }.toString()
            archivedTask.text = datum.archive.count().toString()
            chip.text = datum.tasks.maxByOrNull {
                it.creationTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
            }?.getSHA1()
                ?: context.getString(R.string.no_open_tasks)
            editButton.setOnClickListener { listener(it, datum) }
        }
    }

    override fun getItemCount(): Int = data.size
}