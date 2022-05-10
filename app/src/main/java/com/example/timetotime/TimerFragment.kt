package com.example.timetotime

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.timetotime.databinding.FragmentTimerBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

const val TAG = "TimerFragment"

class TimerFragment : Fragment() {

    private var timerTimeInitList: MutableList<Int> = mutableListOf(0)
    private var timerIntervalInitList: MutableList<Int> = mutableListOf(0)


    private var _binding: FragmentTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)


        //TODO: modify once the associated functions are finished
        _binding!!.newTimerFloatingActionButton.setOnClickListener { newTimerDialog() }
        _binding!!.startStopFloatingActionButton.setOnClickListener { startTimer() }

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

    //TODO: Implement the actual start timer functionality
    private fun startTimer() {

    }

    //TODO: Use the dialog box to create the initial parameters for the timer
    private fun newTimerCard(initialTime: Int = 0, initialInterval: Int = 0) {


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
                timerText.text = timerTimeInitList[index].toString()
                intervalText.text = timerIntervalInitList[index].toString()
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
                    timerTimeInitList.add(timerTime)
                    timerIntervalInitList.add(intervalTime)
                    newTimerCard(timerTime, intervalTime)
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