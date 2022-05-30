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
    val ongoingCommitTime: TextView = itemView.findViewById(R.id.ongoing_commit_time)
    val commitCount: TextView = itemView.findViewById(R.id.commitment_count)
    val commitHours: TextView = itemView.findViewById(R.id.commit_hours)
    val lastCommit: Chip = itemView.findViewById(R.id.last_commit)
    val editButton: Button = itemView.findViewById(R.id.edit_milestone)
    val view: View = itemView
}