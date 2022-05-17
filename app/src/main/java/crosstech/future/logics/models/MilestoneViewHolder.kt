package crosstech.future.logics.models

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import crosstech.future.R

class MilestoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val title: TextView = itemView.findViewById(R.id.titleText)
    val description: TextView = itemView.findViewById(R.id.descText)
    val openTask: TextView = itemView.findViewById(R.id.open_milestone_tasks)
    val closedTask: TextView = itemView.findViewById(R.id.closed_milestone_tasks)
    val archivedTask: TextView = itemView.findViewById(R.id.archived_milestone_tasks)
    val chip: Chip = itemView.findViewById(R.id.milestone_chip)
    val editButton: Button = itemView.findViewById(R.id.edit_milestone)
    val view: View = itemView
}