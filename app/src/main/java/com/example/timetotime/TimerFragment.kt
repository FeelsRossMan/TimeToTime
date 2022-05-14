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

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

const val TAG = "TimerFragment"

class TimerFragment : Fragment() {

    private val timerModel: TimerViewModel by activityViewModels()

    private var _binding: FragmentTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)


        //TODO: modify once the associated functions are finished
        _binding!!.newTimerFloatingActionButton.setOnClickListener { newTimerDialog() }
        _binding!!.startStopFloatingActionButton.setOnClickListener {
            if (!timerStarted) startTimer()
            else stopTimer()
        }


        serviceIntent = Intent(context, TimerService::class.java)
        activity?.registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTimerCard()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // get the current time from the TimerService
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)

        }
    }





    //TODO: Implement the actual start timer functionality
    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        activity?.startService(serviceIntent)
        timerStarted = true
        binding.startStopFloatingActionButton.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    private fun stopTimer() {
        activity?.stopService(serviceIntent)
        binding.startStopFloatingActionButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false
    }

    //TODO: Use the dialog box to create the initial parameters for the timer
    private fun newTimerCard() {


        // Inflate a new timer view using the timer_card.xml layout file
        val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.fab_margin)
        val newTimerView : View = layoutInflater.inflate(R.layout.timer_card, null)
        newTimerView.layoutParams = layoutParams
        with(binding.timerListLinearLayoutView) {
            this.addView(newTimerView,this.childCount-1)

            // Update the values for the time and the interval
            // I'm not sure why, but this function resets all to the default, so i just re-update all of them
            for (index in 0 until childCount) {
                val timerCard = this.getChildAt(index)
                val timerText: TextView = timerCard.findViewById(R.id.running_timer_text)
                val intervalText: TextView = timerCard.findViewById(R.id.running_timer_repeats_text)

                //TODO: Setup a function that takes the int value and converts it to a time string
                timerText.text = timerModel.timerTimeInitList.value?.get(index).toString()
                intervalText.text = timerModel.timerIntervalInitList.value?.get(index).toString()
            }
        }
    }



    private fun newTimerDialog() {
        // Pop up the TimerDialogFragment dialog box to input new timer values
        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.new_timer_dialog, null)

        val timeET = dialogLayout.findViewById<EditText>(R.id.dialog_timer_time_edit_text)
        val intervalET = dialogLayout.findViewById<EditText>(R.id.dialog_timer_interval_edit_text)


        with(builder) {
            setPositiveButton("Set", DialogInterface.OnClickListener{ dialog, id ->
                val timerTime = timeET.text.toString().toIntOrNull()
                val intervalTime = intervalET.text.toString().toIntOrNull()
                Log.d(TAG,"${timeET.text}")

                // Check to see if the dialog box is filled out
                if (timerTime == null || intervalTime == null) {
                    if (timerTime == null && intervalTime != null){
                        Toast.makeText(context, getString(R.string.no_time_set),Toast.LENGTH_SHORT).show()
                    } else if (timerTime != null && intervalTime == null) {
                        Toast.makeText(context, getString(R.string.no_interval_set),Toast.LENGTH_SHORT).show()
                    } else if (timerTime == null && intervalTime == null) {
                        Toast.makeText(context, getString(R.string.nothing_set),Toast.LENGTH_SHORT).show()
                    }
                }

                //Add the new values to the init list, and create a new timer card
                else {
                    timerModel.addNewTimer(timerTime, intervalTime)
                    newTimerCard()
                }
            })

            // Close the dialog box without doing anything
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, id ->

                dialog.dismiss()})
            setView(dialogLayout)
            show()
        }
    }

}