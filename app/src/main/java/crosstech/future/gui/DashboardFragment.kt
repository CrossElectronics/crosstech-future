package crosstech.future.gui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import crosstech.future.R
import crosstech.future.databinding.DashboardFragmentBinding

class DashboardFragment: Fragment(R.layout.dashboard_fragment)
{
    private lateinit var binding: DashboardFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // initialization
        binding = DashboardFragmentBinding.inflate(inflater)

        return binding.root
    }
    // TODO: Implement dashboard
    // Features: Ongoing devotion, scheduled today, next deadline, stats
}