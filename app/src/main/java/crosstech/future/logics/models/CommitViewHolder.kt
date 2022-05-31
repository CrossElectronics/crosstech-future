package crosstech.future.logics.models

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R

class CommitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val hashField: TextView = itemView.findViewById(R.id.hash_field_commit)
    val msgField: TextView = itemView.findViewById(R.id.msg_field_commit)
    val durationField: TextView = itemView.findViewById(R.id.duration_field_commit)
    val timeField: TextView = itemView.findViewById(R.id.time_field_text)
    val view: View = itemView.findViewById(R.id.card_commit)
}