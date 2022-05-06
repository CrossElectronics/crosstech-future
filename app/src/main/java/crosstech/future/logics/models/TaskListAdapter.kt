package crosstech.future.logics.models

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyCallback
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import crosstech.future.R
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.Urgency
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskListAdapter(private val data: List<Task>) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val taskIcon: ImageView = itemView.findViewById(R.id.taskIcon)
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val subType: TextView = itemView.findViewById(R.id.subtitleType)
        val subName: TextView = itemView.findViewById(R.id.subtitleText)
        val hashText: TextView = itemView.findViewById(R.id.hashText)
        val view: View = itemView
    }

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        // TODO: Need a more elegant solution than this. This only solves the problem quick and dirty
        holder.setIsRecyclable(false)
        val item = data[position]
        holder.taskIcon.setImageResource(
            when (item.iconEnum)
            {
                TaskIcon.Planned   -> R.drawable.circle_outline
                TaskIcon.Scheduled -> R.drawable.task_scheduled
                TaskIcon.Deadline  -> R.drawable.deadline
                TaskIcon.Important -> R.drawable.task_important
                TaskIcon.Completed -> R.drawable.task_complete
                TaskIcon.Cancelled -> R.drawable.task_cancelled
            }
        )
        holder.taskName.text = item.name
        holder.hashText.text = item.getSHA1().substring(0 .. 6)
        when (item.iconEnum)
        {
            TaskIcon.Planned   ->
            {
                if (item.urgency != Urgency.Urgent)
                {
                    holder.subName.visibility = View.GONE
                    holder.subType.visibility = View.GONE
                }
                else
                {
                    bindData(holder, item.getTag(), null)
                }
            }
            TaskIcon.Scheduled ->
            {
                bindData(holder, item.getTag(), item.scheduledTime)
            }
            TaskIcon.Important ->
            {
                if (item.scheduledTime != null)
                {
                    bindData(holder, item.getTag(), item.scheduledTime)
                }
                else
                {
                    bindData(holder, item.getTag(), null)
                }
            }
            TaskIcon.Deadline  ->
            {
                bindData(holder, item.getTag(), item.deadline)
            }
            else               ->
            {
                throw IllegalArgumentException("This icon should not be here: ${item.name}")
            }
        }
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    private fun bindData(holder: ViewHolder, type: String, time: LocalDateTime?)
    {
        holder.subType.text = type
        holder.subName.text =
            if (time != null) DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(time)
            else ""
    }
}