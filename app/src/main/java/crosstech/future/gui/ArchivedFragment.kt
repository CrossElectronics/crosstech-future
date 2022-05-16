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
import crosstech.future.databinding.ArchivedFragmentBinding
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.enums.TaskIcon
import crosstech.future.logics.managers.DeleteSwipeManager
import crosstech.future.logics.managers.TasksManager
import crosstech.future.logics.models.ArchivedListAdapter

// TODO: Clean up all the unused fragment data passing
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ArchivedFragment : Fragment(R.layout.archived_fragment)
{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var global: Global
    private lateinit var binding: ArchivedFragmentBinding
    private lateinit var taskRecycler: RecyclerView
    private lateinit var adapter: ArchivedListAdapter

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
        global = requireActivity().applicationContext as Global
        binding = ArchivedFragmentBinding.inflate(layoutInflater)
        taskRecycler = binding.archiveRecycler
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val tasks = TasksManager.sortArchivedTask(global.archive)
        updateHeader()
        adapter = ArchivedListAdapter(tasks)
        taskRecycler.adapter = adapter
        taskRecycler.layoutManager = LinearLayoutManager(activity)

        val deleteSwipeManager =
            ItemTouchHelper(DeleteSwipeManager(view, taskRecycler, global, this))
        deleteSwipeManager.attachToRecyclerView(taskRecycler)
    }

    fun updateHeader(updateSave: Boolean = false)
    {
        binding.cmpArcCount.text =
            global.archive.count { it.iconEnum == TaskIcon.Completed }.toString()
        binding.cncArcCount.text =
            global.archive.count { it.iconEnum == TaskIcon.Cancelled }.toString()
        if (updateSave) global.archive.saveData(Global.ARCHIVE_FILE, global.context)
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArchivedFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArchivedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}