package crosstech.future.logics.models

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskListAdapter(private var data: MutableList<Task>) :
    RecyclerView.Adapter<TaskViewHolder>()
{
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        context = view.context
        return TaskViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int)
    {
        // Issue: scrolling back up messes up bound data
        holder.setIsRecyclable(false)
        val item = data[position]
        holder.taskIcon.setImageResource(
            when (item.iconEnum)
            {
                TaskIcon.Completed -> R.drawable.task_complete
                TaskIcon.Cancelled -> R.drawable.task_cancelled
                TaskIcon.Planned   -> R.drawable.circle_planned
                TaskIcon.Scheduled -> R.drawable.task_scheduled
                TaskIcon.Deadline  -> R.drawable.deadline
                TaskIcon.Important -> R.drawable.task_important
            }
        )
        holder.taskName.text = item.name
        holder.hashText.text = item.getSHA1().substring(0 .. 6)
        if (item.status == TaskStatus.Completed)
        {
            bindData(holder, "Completed", item.completedTime)
            holder.taskIcon.setImageResource(R.drawable.task_complete)
            return
        }
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

    infix fun differAndAddFrom(newData: List<Task>): Int?
    {
        if (newData.size <= data.size || newData == data) return null
        for (i in newData.indices)
        {
            if (i >= data.size || newData[i] != data[i])
            {
                data.add(i, newData[i])
                notifyItemInserted(i)
                return i
            }
        }
        return null
    }

    infix fun removeAt(index: Int)
    {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    fun retrieveData(): MutableList<Task> = data.toMutableList()

    override fun getItemCount(): Int
    {
        return data.size
    }

    private fun bindData(holder: TaskViewHolder, type: String, time: LocalDateTime?)
    {
        holder.subType.text = type
        holder.subName.text =
            if (time != null) DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(time)
            else ""
    }
}