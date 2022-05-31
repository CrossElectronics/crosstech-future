package crosstech.future.gui.milestones

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.FragmentCommitBinding
import crosstech.future.logics.Utils
import crosstech.future.logics.Utils.Companion.toLocalDateTime
import crosstech.future.logics.models.CommitListAdapter
import crosstech.future.logics.models.Milestone
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timer

/**
 * A simple [Fragment] subclass.
 * Use the [CommitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommitFragment : Fragment(R.layout.fragment_commit)
{
    private lateinit var milestone: Milestone
    private lateinit var global: Global
    private lateinit var binding: FragmentCommitBinding
    private lateinit var adapter: CommitListAdapter
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestone = it.getParcelable(ARG_COMMIT)!!
        }
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (timer == null) initTimer()
    }

    override fun onDetach()
    {
        super.onDetach()
        if (timer != null)
        {
            timer!!.cancel()
            timer!!.purge()
            timer = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        global = requireActivity().applicationContext as Global
        binding = FragmentCommitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if (timer == null) initTimer()
        with(binding) {
            msTitle.text = milestone.name
            msDescription.text = milestone.description
            updateOngoingCommit()
            adapter = CommitListAdapter(milestone.commits)
            commitRecycler.adapter = adapter
            commitRecycler.layoutManager = LinearLayoutManager(context)

            emptyListBtn.setOnClickListener(addCommitOnClick)
            addCommitFab.setOnClickListener(addCommitOnClick)

            commitRecycler.addOnScrollListener(
                // TODO: Move this and OpenTaskFragment.kt 's listener together
                // Clean up the code
                object : RecyclerView.OnScrollListener()
                {
                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int
                    )
                    {
                        if (dy > 0) addCommitFab.hide()
                        else addCommitFab.show()
                    }
                }
            )
        }
    }

    private fun initTimer()
    {
        timer = timer(period = 1000L) {
            Handler(Looper.getMainLooper()).post {
                if (milestone.ongoingCommit == null) return@post
                val duration =
                    Duration.between(milestone.ongoingCommit?.toLocalDateTime(),
                                     LocalDateTime.now())
                        .seconds
                binding.devotionTimer.text = String.format(
                    "%02d:%02d:%02d",
                    duration / 3600,
                    (duration % 3600) / 60,
                    duration % 60
                )
            }
        }
    }

    private fun FragmentCommitBinding.updateOngoingCommit()
    {
        if (milestone.ongoingCommit == null)
        {
            ongoingCard.visibility = View.GONE
        }
        else
        {
            ongoingCard.visibility = View.VISIBLE
            ongoingStart.text =
                milestone.ongoingCommit!!.toLocalDateTime().format(Utils.fullFormatter)
        }
        emptyListBtn.visibility =
            if (milestone.commits.size == 0 && milestone.ongoingCommit == null) View.VISIBLE
            else View.GONE
        addCommitFab.isVisible = milestone.ongoingCommit == null
    }

    private fun setButtonAvailability(dateField: EditText, timeField: EditText, button: Button)
    {
        val timeEntered = Utils.parseTime(dateField, timeField)
        button.isEnabled = timeEntered.isBefore(LocalDateTime.now())
    }

    private val addCommitOnClick = View.OnClickListener {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(),
                                                       R.style.ThemeOverlay_App_MaterialAlertDialog)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_datetime_picker, null, false)

        val datePickerField: EditText = dialogView.findViewById(R.id.date_picker)
        datePickerField.setText(LocalDateTime.now().format(Utils.dateOnlyFormatter))
        val timePickerField: EditText = dialogView.findViewById(R.id.time_picker)
        timePickerField.setText(LocalDateTime.now().format(Utils.timeOnlyFormatter))
        val messageFieldContainer: View = dialogView.findViewById(R.id.commit_msg_container)
        messageFieldContainer.visibility = View.GONE
        val calRest =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

        datePickerField.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.starting_commit_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(calRest.build())
                .build()
            datePicker.show(requireActivity().supportFragmentManager, "")
            datePicker.addOnPositiveButtonClickListener {
                datePickerField.setText(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd").format(
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(it),
                            TimeZone.getDefault().toZoneId()
                        )
                    )
                )
            }
        }

        timePickerField.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(0)
                    .setMinute(0)
                    .setTitleText(getString(R.string.at))
                    .build()
            timePicker.show(requireActivity().supportFragmentManager, "")
            timePicker.addOnPositiveButtonClickListener {
                timePickerField.setText(
                    String.format(
                        "%02d:%02d",
                        timePicker.hour,
                        timePicker.minute
                    )
                )
            }
        }

        dialogBuilder.apply {
            setView(dialogView)
            setTitle(getString(R.string.start_a_new_commit))
            setPositiveButton(getString(R.string.save)) { _, _ ->
                milestone.ongoingCommit =
                    Utils.parseTime(datePickerField, timePickerField, LocalDateTime.now().second)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                binding.updateOngoingCommit()
            }
            setNegativeButton(getString(R.string.cancel), null)
        }

        val dialog = dialogBuilder.create()
        dialog.show()

        datePickerField.addTextChangedListener {
            setButtonAvailability(datePickerField,
                                  timePickerField,
                                  dialog.getButton(AlertDialog.BUTTON_POSITIVE))
        }
        timePickerField.addTextChangedListener {
            setButtonAvailability(datePickerField,
                                  timePickerField,
                                  dialog.getButton(AlertDialog.BUTTON_POSITIVE))
        }
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param milestone parameter to pass
         * @return A new instance of fragment CommitFragment.
         */
        @JvmStatic
        fun newInstance(milestone: Milestone) =
            CommitFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COMMIT, milestone)
                }
            }

        private const val ARG_COMMIT = "milestone"
    }
}