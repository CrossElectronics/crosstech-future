package crosstech.future.gui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import crosstech.future.Global
import crosstech.future.R
import crosstech.future.databinding.TaskEditFragmentBinding
import crosstech.future.logics.enums.TaskStatus
import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.Task
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "parcel"
private const val ARG_PARAM2 = "mode"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskEditFragment : DialogFragment(), Toolbar.OnMenuItemClickListener
{
    private lateinit var binding: TaskEditFragmentBinding

    private var task: Task? = null
    private var mode: Boolean? = null
    private lateinit var global: Global

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            task = it.getParcelable(ARG_PARAM1) as Task?
            mode = it.getBoolean(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = TaskEditFragmentBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        global = requireActivity().applicationContext as Global
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnMenuItemClickListener(this)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.task_edit_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val actionBar = requireActivity().actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        super.onViewCreated(view, savedInstanceState)
        val calRestriction =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
        val task = task!!
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        with(binding)
        {
            // initialization
            nameEditText.setText(task.name)
            descEditText.setText(task.description)
            scheduleLayout.visibility = if (task.scheduledTime == null) View.GONE else View.VISIBLE
            deadlineLayout.visibility = if (task.deadline == null) View.GONE else View.VISIBLE
            urgencySlider.value = when (task.urgency)
            {
                Urgency.Casual -> 1f
                Urgency.Normal -> 2f
                Urgency.Urgent -> 3f
            }
            difficultySlider.value = task.estDifficulty.toFloat()
            scheduleSw.isChecked = task.scheduledTime != null
            importanceSw.isChecked = task.isImportant
            creationDateText.setText(yearFormatter.format(task.creationTime))
            creationTimeText.setText(timeFormatter.format(task.creationTime))
            if (task.scheduledTime != null)
            {
                plannedDateText.setText(yearFormatter.format(task.scheduledTime))
                plannedTimeText.setText(timeFormatter.format(task.scheduledTime))
            }
            else
            {
                plannedDateText.setText(yearFormatter.format(task.creationTime.plusDays(1)))
                plannedTimeText.setText("00:00")
            }
            if (task.deadline != null)
            {
                deadlineDateText.setText(yearFormatter.format(task.deadline))
                deadlineTimeText.setText(timeFormatter.format(task.deadline))
            }
            else
            {
                deadlineDateText.setText(yearFormatter.format(task.creationTime.plusDays(1)))
                deadlineTimeText.setText("00:00")
            }
            updateSHA1(task)
            // listeners
            nameEditText.addTextChangedListener {
                task.name = it.toString()
                updateSHA1(task)
            }
            descEditText.addTextChangedListener {
                task.description = it.toString()
                updateSHA1(task)
            }
            creationDateText.isEnabled = false
            creationTimeText.isEnabled = false
            urgencySlider.setLabelFormatter {
                when (it)
                {
                    1f   -> "CASUAL"
                    2f   -> "NORMAL"
                    3f   -> "URGENT"
                    else -> it.toString()
                }
            }
            urgencySlider.addOnSliderTouchListener(
                object : Slider.OnSliderTouchListener
                {
                    @SuppressLint("RestrictedApi")
                    override fun onStartTrackingTouch(slider: Slider)
                    {
                    }

                    @SuppressLint("RestrictedApi")
                    override fun onStopTrackingTouch(slider: Slider)
                    {
                        task.urgency = when (slider.value)
                        {
                            1f   -> Urgency.Casual
                            2f   -> Urgency.Normal
                            3f   -> Urgency.Urgent
                            else -> throw Exception("Illegal slider value")
                        }
                    }
                })
            difficultySlider.setLabelFormatter {
                val grades = arrayOf("E", "E+", "D", "D+", "C-", "C", "C+", "B", "B+", "A", "A+")
                grades[it.toInt()]
            }
            difficultySlider.addOnSliderTouchListener(
                object : Slider.OnSliderTouchListener
                {
                    @SuppressLint("RestrictedApi")
                    override fun onStartTrackingTouch(slider: Slider)
                    {
                    }

                    @SuppressLint("RestrictedApi")
                    override fun onStopTrackingTouch(slider: Slider)
                    {
                        task.estDifficulty = slider.value.toInt()
                    }
                })
            importanceSw.setOnCheckedChangeListener { _, b -> task.isImportant = b }
            scheduleSw.setOnCheckedChangeListener { _, b ->
                scheduleLayout.visibility = if (b) View.VISIBLE else View.GONE
                if (b)
                {
                    task.scheduledTime = parseTime(plannedDateText, plannedTimeText)
                    task.status = TaskStatus.Scheduled
                }
                else
                {
                    task.scheduledTime = null
                    task.deadline = null
                    task.status = TaskStatus.Planned
                }
            }
            plannedDateText.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.plan_to_do))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calRestriction.build())
                    .build()
                datePicker.show(requireActivity().supportFragmentManager, "")
                datePicker.addOnPositiveButtonClickListener {
                    plannedDateText.setText(
                        yearFormatter.format(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(datePicker.selection!!),
                                TimeZone.getDefault().toZoneId()
                            )
                        )
                    )
                    task.scheduledTime = parseTime(plannedDateText, plannedTimeText)
                }
            }
            plannedTimeText.setOnClickListener {
                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setHour(0)
                        .setMinute(0)
                        .setTitleText(getString(R.string.at_around))
                        .build()
                timePicker.show(requireActivity().supportFragmentManager, "")
                timePicker.addOnPositiveButtonClickListener {
                    plannedTimeText.setText(
                        String.format(
                            "%02d:%02d",
                            timePicker.hour,
                            timePicker.minute
                        )
                    )
                    task.scheduledTime = parseTime(plannedDateText, plannedTimeText)
                    if (task.scheduledTime!! <= task.creationTime)
                    {
                        plannedDateText.setText(yearFormatter.format(task.creationTime.plusDays(1)))
                        plannedTimeText.setText("00:00")
                        task.scheduledTime = parseTime(plannedDateText, plannedTimeText)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.invalid_time_planned_creation),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            deadlineSw.setOnCheckedChangeListener { _, b ->
                deadlineLayout.visibility = if (b) View.VISIBLE else View.GONE
                if (b)
                {
                    task.scheduledTime = parseTime(plannedDateText, plannedTimeText)
                    task.deadline = parseTime(deadlineDateText, deadlineTimeText)
                }
                else
                {
                    task.deadline = null
                }
            }
            deadlineDateText.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.deadline_on))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calRestriction.build())
                    .build()
                datePicker.show(requireActivity().supportFragmentManager, "")
                datePicker.addOnPositiveButtonClickListener {
                    deadlineDateText.setText(
                        yearFormatter.format(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(datePicker.selection!!),
                                TimeZone.getDefault().toZoneId()
                            )
                        )
                    )
                    task.deadline = parseTime(deadlineDateText, deadlineTimeText)
                }
            }
            deadlineTimeText.setOnClickListener {
                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setHour(0)
                        .setMinute(0)
                        .setTitleText(getString(R.string.at_around))
                        .build()
                timePicker.show(requireActivity().supportFragmentManager, "")
                timePicker.addOnPositiveButtonClickListener {
                    deadlineTimeText.setText(
                        String.format(
                            "%02d:%02d",
                            timePicker.hour,
                            timePicker.minute
                        )
                    )
                    task.deadline = parseTime(deadlineDateText, deadlineTimeText)
                    if (task.deadline as LocalDateTime <= task.scheduledTime)
                    {
                        deadlineDateText.setText(yearFormatter.format(task.scheduledTime?.plusDays(1)))
                        deadlineTimeText.setText("00:00")
                        task.deadline = parseTime(deadlineDateText, deadlineTimeText)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.invalid_time_ddl_scheduled),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            // Reminds user before starting, starting, and before deadline
        }
    }

    private fun parseTime(d: TextInputEditText, t: TextInputEditText): LocalDateTime
    {
        return LocalDateTime.parse(d.text.toString() + "T" + t.text.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun updateSHA1(task: Task)
    {
        binding.taskEditHashText.text = "#${task.getSHA1().substring(0 .. 9)}..."
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskEditFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean
    {
        if (task == null) return true
        val task = task as Task
        when (item?.itemId)
        {
            R.id.save ->
            {
                if (task.name == "")
                {
                    Toast.makeText(context, getString(R.string.invalid_name), Toast.LENGTH_LONG)
                        .show()
                    return true
                }
                if (task.scheduledTime != null && task.scheduledTime!! <= task.creationTime)
                {
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_time_planned_creation),
                        Toast.LENGTH_LONG
                    ).show()
                    return true
                }
                if (task.deadline != null && task.scheduledTime != null && task.deadline!! <= task.scheduledTime)
                {
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_time_ddl_scheduled),
                        Toast.LENGTH_LONG
                    ).show()
                    return true
                }
                if (mode == true)
                {
                    global.tasks.add(task)
                    dismiss()
                }

                val frag =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.taskInnerFrag)
                if (frag is OpenTaskFragment)
                    frag.notifyUpdate()
                // Needs to edit the task, schedule it and whatnot
            }
        }
        return true
    }
}