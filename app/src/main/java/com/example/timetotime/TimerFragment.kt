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
import androidx.fragment.app.Fragment
import com.example.timetotime.databinding.FragmentTimerBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

const val TAG = "TimerFragment"

class TimerFragment : Fragment() {

    private var timerInitList: MutableList<Int> = mutableListOf(0)


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
            for (index in 0 until childCount) {
                val timerCard = this.getChildAt(index)
                val timerText: TextView = timerCard.findViewById(R.id.running_timer_text)

                //TODO: Setup a function that takes the int value and converts it to a time string
                timerText.text = timerInitList[index].toString()
            }
        }
    }
    private fun newTimerDialog() {
        // Pop up the TimerDialogFragment dialog box to input new timer values
        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.new_timer_dialog, null)

        val editText = dialogLayout.findViewById<EditText>(R.id.dialog_timer_time_edit_text)


        with(builder) {
            setPositiveButton("Set", DialogInterface.OnClickListener{ dialog, id ->
                val textInt = editText.text.toString().toIntOrNull()
                Log.d(TAG,"${editText.text}")
                if (textInt == null) {

                    dialog.dismiss()
                }
                else {
                    timerInitList.add(textInt)
                    newTimerCard(textInt)
                }
            })
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, id ->

                dialog.dismiss()})
            setView(dialogLayout)
            show()
        }
    }
}