package crosstech.future.logics.models

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.R
import crosstech.future.logics.Utils
import java.time.format.DateTimeFormatter

class CommitListAdapter(var data: MutableList<Commit>) : RecyclerView.Adapter<CommitViewHolder>()
{
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder
    {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.commit_card, parent, false)
        context = view.context
        return CommitViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int)
    {
        val datum = data[position]
        with(holder) {
            hashField.text = datum.getSHA1().substring(0 .. 6)
            msgField.text = datum.commitMessage
            durationField.text = String.format("%.2f", datum.getDuration()) + " h"
            timeField.text =
                String.format(datum.getStartTime().format(Utils.fullFormatter) + " ~ "
                              + datum.getEndTime().format(Utils.timeOnlyFormatter))
        }
    }

    override fun getItemCount(): Int = data.size
}