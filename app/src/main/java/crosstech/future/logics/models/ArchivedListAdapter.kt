package crosstech.future.logics.models

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R
import crosstech.future.logics.enums.TaskIcon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArchivedListAdapter(var data: MutableList<ArchivedTask>) :
    RecyclerView.Adapter<TaskViewHolder>()
{
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        context = view.context
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int)
    {
        holder.setIsRecyclable(false)
        val item = data[position]
        holder.taskIcon.setImageResource(
            when (item.iconEnum)
            {
                TaskIcon.Completed -> R.drawable.task_complete
                TaskIcon.Cancelled -> R.drawable.task_cancelled
                else               -> throw IllegalStateException("Unacceptable archive type ${item.iconEnum}")
            }
        )
        holder.taskName.text = item.name
        holder.hashText.text = item.sha1.substring(0 .. 6)
        when (item.iconEnum)
        {
            TaskIcon.Cancelled -> bindData(holder, "Cancelled", item.completeTime)
            TaskIcon.Completed -> bindData(holder, "Completed", item.completeTime)
            else               -> throw IllegalStateException("Unacceptable archive type ${item.iconEnum}")
        }
    }

    private fun bindData(holder: TaskViewHolder, s: String, time: LocalDateTime)
    {
        holder.subType.text = s
        holder.subName.text = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(time)
    }

    override fun getItemCount(): Int = data.size
}