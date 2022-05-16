package crosstech.future.gui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.CompletedTaskFragmentBinding
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.managers.ArchiveSwipeManager
import crosstech.future.logics.managers.ReopenSwipeManager
import crosstech.future.logics.managers.TasksManager
import crosstech.future.logics.models.TaskListAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CompletedTaskFragment : Fragment(R.layout.completed_task_fragment)
{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: CompletedTaskFragmentBinding
    private lateinit var global: Global
    private lateinit var adapter: TaskListAdapter
    private lateinit var taskRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = CompletedTaskFragmentBinding.inflate(layoutInflater)
        taskRecycler = binding.closedRecycler
        global = activity?.applicationContext as Global
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val tasks = TasksManager.filterCompletedTaskAndSort(global.tasks)
        updateHeader()
        adapter = TaskListAdapter(tasks) {
            // TODO: Add task viewing for completed task
        }
        taskRecycler.adapter = adapter
        taskRecycler.layoutManager = LinearLayoutManager(activity)

        val reopenSwpMng = ItemTouchHelper(ReopenSwipeManager(view, taskRecycler, global, this))
        reopenSwpMng.attachToRecyclerView(taskRecycler)
        val archiveSwpMng = ItemTouchHelper(ArchiveSwipeManager(view, taskRecycler, global, this))
        archiveSwpMng.attachToRecyclerView(taskRecycler)
    }

    fun updateHeader(updateTask: Boolean = false, updateArchive: Boolean = false)
    {
        binding.completedCount.text =
            global.tasks.count { it.status == TaskStatus.Completed }.toString()
        if (updateTask) global.tasks.saveData(Global.TASKS_FILE, global.context)
        if (updateArchive) global.archive.saveData(Global.ARCHIVE_FILE, global.context)
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompletedFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompletedTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}