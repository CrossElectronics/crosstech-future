package crosstech.future.gui.milestones

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.MilestonesFragmentBinding
import crosstech.future.logics.models.MilestoneListAdapter

class MilestoneFragment : Fragment(R.layout.milestones_fragment)
{
    private lateinit var binding: MilestonesFragmentBinding
    private lateinit var global: Global
    private lateinit var adapter: MilestoneListAdapter
    private lateinit var milestoneRecycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = MilestonesFragmentBinding.inflate(layoutInflater)
        global = requireActivity().applicationContext as Global
        milestoneRecycler = binding.milestoneRecycler
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val milestones = global.milestones
        adapter = MilestoneListAdapter(milestones) { v, milestone -> }
        milestoneRecycler.adapter = adapter
        milestoneRecycler.layoutManager = LinearLayoutManager(activity)
    }
}