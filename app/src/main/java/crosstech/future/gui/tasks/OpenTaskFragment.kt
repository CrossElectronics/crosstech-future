package crosstech.future.gui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.OpenTaskFragmentBinding
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.managers.*
import crosstech.future.logics.models.Task
import crosstech.future.logics.models.TaskListAdapter

class OpenTaskFragment : Fragment(R.layout.open_task_fragment)
{
    private lateinit var binding: OpenTaskFragmentBinding
    private lateinit var global: Global
    private lateinit var adapter: TaskListAdapter
    private lateinit var taskRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = OpenTaskFragmentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.open_task_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        global = activity?.applicationContext as Global
        val tasks = TasksManager.filterOpenTasksAndSort(global.tasks)
        updateHeader()
        adapter = TaskListAdapter(tasks) {
            pullTaskEditor(it, false)
        }
        taskRecycler = binding.taskRecycler
        val fab = binding.addTaskFab
        taskRecycler.adapter = adapter
        taskRecycler.layoutManager = LinearLayoutManager(activity)
        // make the fab hide when scrolling down, appear scrolling up
        taskRecycler.addOnScrollListener(
            object : OnScrollListener()
            {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                )
                {
                    if (dy > 0) fab.hide()
                    if (dy < 0) fab.show()
                }
            })

        val leftSwpMng = ItemTouchHelper(CancelSwipeManager(view, taskRecycler, global, this))
        leftSwpMng.attachToRecyclerView(taskRecycler)
        val rightSwpMng = ItemTouchHelper(CompleteSwipeManager(view, taskRecycler, global, this))
        rightSwpMng.attachToRecyclerView(taskRecycler)

        fab.setOnClickListener {
            pullTaskEditor(Task(), true)
        }
    }

    private fun pullTaskEditor(task: Task, isNew: Boolean)
    {
        val newTaskFrag = TaskEditFragment()
        val bundle = Bundle()
        bundle.putParcelable("parcel", task)
        bundle.putBoolean("mode", isNew)
        newTaskFrag.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(R.id.content, newTaskFrag).addToBackStack(null).commit()
    }

    fun updateHeader(updateTasks: Boolean = false, updateArchive: Boolean = false)
    {
        binding.plannedCount.text =
            global.tasks.count { it.status == TaskStatus.Planned }.toString()
        binding.scheduledCount.text =
            global.tasks.count { it.status == TaskStatus.Scheduled }.toString()
        if (updateTasks) global.tasks.saveData(Global.TASKS_FILE, global.context)
        if (updateArchive) global.archive.saveData(Global.ARCHIVE_FILE, global.context)
    }

    fun notifyAdd()
    {
        val tasks = TasksManager.filterOpenTasksAndSort(global.tasks)
        val i = adapter differAndAddFrom tasks
        if (i != null) taskRecycler.scrollToPosition(i)
        updateHeader()
        global.tasks.saveData(Global.TASKS_FILE, global.context)
    }

    fun notifyChange(index: Int)
    {
        val item = global.tasks[index]
        val tasks = TasksManager.filterOpenTasksAndSort(global.tasks)
        val currentIndex = tasks.indexOf(item)
        adapter.data = tasks
        // TODO: Use notifyItemChanged(currentIndex)
        // Need to use setHasStableIds and override getItemId
        adapter.notifyDataSetChanged()
        taskRecycler.scrollToPosition(currentIndex)
        updateHeader()
        global.tasks.saveData(Global.TASKS_FILE, global.context)
    }
}