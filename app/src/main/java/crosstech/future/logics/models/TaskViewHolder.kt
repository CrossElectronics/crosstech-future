package crosstech.future.logics.models

import android.view.SubMenu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val taskIcon: ImageView = itemView.findViewById(R.id.taskIcon)
    val taskName: TextView = itemView.findViewById(R.id.taskName)
    val subType: TextView = itemView.findViewById(R.id.subtitleType)
    val subName: TextView = itemView.findViewById(R.id.subtitleText)
    val hashText: TextView = itemView.findViewById(R.id.hashText)
    val view: View = itemView
}