package crosstech.future.gui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.TasksFragmentBinding

class TasksFragment : Fragment(R.layout.tasks_fragment)
{
    private lateinit var global: Global
    private lateinit var binding: TasksFragmentBinding
    private lateinit var tabBar: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TasksFragmentBinding.inflate(layoutInflater)
        tabBar = binding.tabLayout

        val openTaskFragment = OpenTaskFragment()
        val closedTaskFragment = CompletedTaskFragment()

        tabBar.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener
            {
                override fun onTabSelected(tab: TabLayout.Tab?)
                {
                    when (tab?.position)
                    {
                        0 ->
                            activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(R.id.taskInnerFrag, openTaskFragment)
                                ?.commit()
                        1 ->
                            activity?.supportFragmentManager
                                ?.beginTransaction()
                                ?.replace(R.id.taskInnerFrag, closedTaskFragment)
                                ?.commit()
                        2 ->
                            TODO("Archived tab")
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?)
                {
                }

                override fun onTabReselected(tab: TabLayout.Tab?)
                {
                }
            })

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.taskInnerFrag, openTaskFragment)
            ?.commit()

        // get data
        global = activity?.applicationContext as Global
        return binding.root
    }
}