package crosstech.future.gui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.TasksFragmentBinding
import crosstech.future.logics.models.Task
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class TasksFragment : Fragment(R.layout.tasks_fragment)
{
    private lateinit var global: Global
    private lateinit var binding: TasksFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TasksFragmentBinding.inflate(layoutInflater)
        // get data
        global = activity?.applicationContext as Global
        return binding.root
    }
}