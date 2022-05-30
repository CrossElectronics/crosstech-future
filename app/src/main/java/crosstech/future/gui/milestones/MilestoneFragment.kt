package crosstech.future.gui.milestones

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.MilestonesFragmentBinding
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.models.Milestone
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
        adapter = MilestoneListAdapter(
            milestones,
            editListener = { _, m ->
                buildDialog(requireContext().getString(R.string.create_milestone), m)
            },
            cardListener = { _, m ->

            }
        )
        milestoneRecycler.adapter = adapter
        milestoneRecycler.layoutManager = LinearLayoutManager(activity)
        binding.addMilestoneFab.setOnClickListener {
            buildDialog(requireContext().getString(R.string.create_milestone))
        }
    }

    private fun buildDialog(title: String,
                            toReplace: Milestone? = null)
    {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(),
                                                       R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_milestone, null, false)
        val nameField: EditText = dialogView.findViewById(R.id.milestone_name)
        val descField: EditText = dialogView.findViewById(R.id.milestone_desc)
        nameField.setText(toReplace?.name)
        descField.setText(toReplace?.description)
        dialogBuilder.apply {
            setView(dialogView)
            setTitle(title)
            setPositiveButton(getString(R.string.save)) { _, _ ->
                val milestone = Milestone(nameField.text.toString(),
                                          descField.text.toString(),
                                          mutableListOf(), null)
                if (toReplace != null) global.milestones.remove(toReplace)
                global.milestones.add(milestone)
                adapter.data = global.milestones
                val index = global.milestones.indexOf(milestone)
                if (toReplace == null) adapter.notifyItemInserted(index)
                else adapter.notifyItemChanged(index)
                milestoneRecycler.scrollToPosition(index)
                global.milestones.saveData(Global.MILESTONES_FILE, context)
            }
            setNegativeButton(getString(R.string.cancel), null)
        }
        val dialog = dialogBuilder.create()
        dialog.show()
        val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.isEnabled = false
        fun updateButton()
        {
            positiveBtn.isEnabled =
                !(nameField.text.toString() == "" || descField.text.toString() == "")
        }
        nameField.addTextChangedListener { updateButton() }
        descField.addTextChangedListener { updateButton() }
    }
}