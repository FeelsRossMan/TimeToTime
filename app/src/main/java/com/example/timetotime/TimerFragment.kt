package com.example.timetotime

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.timetotime.databinding.FragmentTimerBinding


const val TAG = "TimerFragment"

class TimerFragment : Fragment() {

    private val timerModel: TimerViewModel by activityViewModels()

    private var _binding: FragmentTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)


        //TODO: modify once the associated functions are finished

        _binding!!.newTimerFloatingActionButton.setOnClickListener { newTimer() }
        _binding!!.startStopFloatingActionButton.setOnClickListener {
            if (!timerModel.timerStarted) startTimer()
            else stopTimer()
        }
        _binding!!.resetTimerFloatingActionButton.setOnClickListener { resetTimer() }
        // Set the floating action buttons based on if the timer is running or not
        if (!timerModel.timerCompleted) {
            if (timerModel.timerStarted) _binding!!.startStopFloatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24)
            else _binding!!.startStopFloatingActionButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }

        serviceIntent = Intent(context, TimerService::class.java)
        activity?.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recreateTimerCardViews()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // get the current time from the TimerService
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            // Add the remaining time to the timerModel from the service
            //TODO: modify this so that it calculates the correct time to display, even if the app was minimized
            timerModel.activeTimerSecondsRemaining--

            var textTime = if (timerModel.activeTimerSecondsRemaining > 0) timerModel.activeTimerSecondsRemaining
            else 0
            if (textTime < 0) textTime = 0

            _binding?.timerListLinearLayoutView?.getChildAt(timerModel.activeTimer)
                ?.findViewById<TextView>(R.id.running_timer_text)
                ?.text = getTimeStringFromInt(textTime)
            _binding?.timerListLinearLayoutView?.getChildAt(timerModel.activeTimer)
                ?.findViewById<TextView>(R.id.running_timer_repeats_text)
                ?.text = timerModel.activeIntervalsRemaining.toString()
            if(timerModel.checkIfTimerFinished()) stopTimer()
        }
    }

    // function called when the start button is pushed
    private fun startTimer() {
        if (timerModel.timerCompleted)
            return
        serviceIntent.putExtra(TimerService.TIME_EXTRA, timerModel.activeTimerTimeElapsed.toDouble())
        activity?.startService(serviceIntent)
        timerModel.timerStarted = true
        binding.startStopFloatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    // function called when the pause button is pushed
    private fun stopTimer() {
        activity?.stopService(serviceIntent)
        _binding?.startStopFloatingActionButton?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        timerModel.timerStarted = false
    }

    // create a new timer card
    private fun newTimerCard() {


        // Inflate a new timer view using the timer_card.xml layout file
        val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.fab_margin)
        val newTimerView : View = layoutInflater.inflate(R.layout.timer_card, null)
        newTimerView.layoutParams = layoutParams
        with(binding.timerListLinearLayoutView) {
            this.addView(newTimerView,this.childCount-1)
        }
    }

    // reset the timers to their init values from the timerModel
    private fun setTimersToCurrentValues() {
        with(binding.timerListLinearLayoutView) {
            // Update the values for the time and the interval
            for (childCountIndex in 0 until childCount) {
                Log.d(TAG, "current: ${childCountIndex} out of ${childCount}")
                val timerCard = this.getChildAt(childCountIndex)
                val timerText: TextView = timerCard.findViewById(R.id.running_timer_text)
                val intervalText: TextView = timerCard.findViewById(R.id.running_timer_repeats_text)

                //TODO: Setup a function that takes the int value and converts it to a time string
                if (childCountIndex == timerModel.activeTimer) {
                    timerText.text = getTimeStringFromInt(setToZeroIfNegative(timerModel.activeTimerSecondsRemaining))
                    intervalText.text = timerModel.activeIntervalsRemaining.toString()
                } else if (childCountIndex < timerModel.activeTimer) {
                    timerText.text = getTimeStringFromInt(0)
                    intervalText.text = "0"
                } else {
                    timerText.text = getTimeStringFromInt(timerModel.timerTimeInitList[childCountIndex])
                    intervalText.text = timerModel.timerIntervalInitList[childCountIndex].toString()
                    print("")
                }
            }
        }
    }

    // recreate timercard views for when the UI is destroyed
    private fun recreateTimerCardViews() {
        binding.timerListLinearLayoutView.removeAllViews()
        for (index in 0 until timerModel.timerTimeInitList.size) {
            newTimerCard()
        }
        setTimersToCurrentValues()
    }

    // newTimer button was pushed
    private fun newTimer() {
        if (timerModel.timerStarted) return
        if (timerModel.timerCompleted) timerModel.resetAllTimersToInit()
        newTimerDialog()
    }

    // create the dialog popup to enter new timer values
    private fun newTimerDialog() {
        // Pop up the TimerDialogFragment dialog box to input new timer values
        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.new_timer_dialog, null)

        val timeMinutesET = dialogLayout.findViewById<EditText>(R.id.dialog_timer_time_edit_text_minutes)
        val timeSecondsET = dialogLayout.findViewById<EditText>(R.id.dialog_timer_time_edit_text_seconds)
        val intervalET = dialogLayout.findViewById<EditText>(R.id.dialog_timer_interval_edit_text)


        with(builder) {
            setPositiveButton("Set") { _, _ ->
                val timerMinTime = timeMinutesET.text.toString().toIntOrNull()
                val timerSecTime = timeSecondsET.text.toString().toIntOrNull()
                val intervalTime = intervalET.text.toString().toIntOrNull()
                Log.d(TAG, "${timeMinutesET.text}")

                // Check to see if the dialog box is filled out
                if ((timerMinTime == null && timerSecTime == null) || intervalTime == null) {

                    // no time set
                    if (timerMinTime == null && timerSecTime == null && intervalTime != null) {
                        Toast.makeText(context, getString(R.string.no_time_set), Toast.LENGTH_SHORT)
                            .show()
                        // no interval set
                    } else if ((timerMinTime != null || timerSecTime != null) && intervalTime == null) {
                        Toast.makeText(
                            context,
                            getString(R.string.no_interval_set),
                            Toast.LENGTH_SHORT
                        ).show()
                        // nothing set
                    } else if (timerMinTime == null && timerSecTime == null && intervalTime == null) {
                        Toast.makeText(context, getString(R.string.nothing_set), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                //Add the new values to the init list, and create a new timer card
                else {
                    timerModel.addNewTimer(timerMinTime, timerSecTime, intervalTime)
                    newTimerCard()
                    setTimersToCurrentValues()
                }
            }

            // Close the dialog box without doing anything
            setNegativeButton("Cancel") { dialog, _ ->

                dialog.dismiss()
            }
            setView(dialogLayout)
            show()
        }
    }

    // reset timer button was clicked
    private fun resetTimer() {
        if (timerModel.timerStarted) return
        timerModel.resetAllTimersToInit()
        recreateTimerCardViews()
    }

    // convert an int (seconds) to a time string
    private fun getTimeStringFromInt(time: Int) : String{
        val resultInt = time
        val minutes = (resultInt / 60)
        val seconds = resultInt % 60
        fun makeTimeString(minutes: Int, seconds: Int): String = String.format("%2d:%02d", minutes, seconds)

        return makeTimeString(minutes, seconds)
    }

    private fun setToZeroIfNegative(theInt: Int): Int {
        return if (theInt < 0) 0
        else theInt
    }
}