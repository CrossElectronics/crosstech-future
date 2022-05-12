package crosstech.future.gui

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OpenTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenTaskFragment : Fragment(R.layout.open_task_fragment)
{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: OpenTaskFragmentBinding
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
        adapter = TaskListAdapter(tasks)
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
            val newTaskFrag = TaskEditFragment()
            val bundle = Bundle()
            bundle.putParcelable("parcel", Task())
            bundle.putBoolean("mode", true)
            newTaskFrag.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.content, newTaskFrag).addToBackStack(null).commit()
        }
    }

    fun updateHeader(update: Boolean = false)
    {
        binding.plannedCount.text =
            global.tasks.count { it.status == TaskStatus.Planned }.toString()
        binding.scheduledCount.text =
            global.tasks.count { it.status == TaskStatus.Scheduled }.toString()
        if (update) global.tasks.saveData(Global.TASKS_FILE, global.context)
    }

    fun notifyUpdate()
    {
        val tasks = TasksManager.filterOpenTasksAndSort(global.tasks)
        val i = adapter differAndAddFrom tasks
        if (i != null) taskRecycler.scrollToPosition(i)
        updateHeader()
        global.tasks.saveData(Global.TASKS_FILE, global.context)
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpenTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}