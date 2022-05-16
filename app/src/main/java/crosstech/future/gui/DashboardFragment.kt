package crosstech.future.gui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.DashboardFragmentBinding
import crosstech.future.logics.Utils.Companion.getSaveSize
import crosstech.future.logics.Utils.Companion.toReadable

class DashboardFragment: Fragment(R.layout.dashboard_fragment)
{
    private lateinit var binding: DashboardFragmentBinding
    private lateinit var global: Global
    private lateinit var activity: Activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // initialization
        binding = DashboardFragmentBinding.inflate(inflater)
        activity = requireActivity()
        global = activity.applicationContext as Global
        return binding.root
    }
    // TODO: Implement dashboard
    // Features: Ongoing devotion, scheduled today, next deadline, stats

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        updateSize()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?)
    {
        super.onViewStateRestored(savedInstanceState)
        updateSize()
    }

    private fun updateSize()
    {
        val size =
            Global.TASKS_FILE.getSaveSize(activity) + Global.ARCHIVE_FILE.getSaveSize(activity)
        binding.tvTotalUsage.text = size.toReadable()
    }
}