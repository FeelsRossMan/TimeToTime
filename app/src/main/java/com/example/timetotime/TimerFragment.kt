package com.example.timetotime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.timetotime.databinding.FragmentTimerBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TimerFragment : Fragment() {



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
        _binding!!.newTimerFloatingActionButton.setOnClickListener { newTimerCard() }
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
        val timerDialog = TimerDialogFragment()
        timerDialog.show(childFragmentManager, TimerDialogFragment.Tag)
    }

    //TODO: Use the dialog box to create the initial parameters for the timer
    private fun newTimerCard() {
        val inflater = LayoutInflater.from(context)
        val layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.fab_margin)
        val newTimerView : View = inflater.inflate(R.layout.timer_card, null)
        newTimerView.layoutParams = layoutParams
        with(binding.timerListLinearLayoutView) {
            this.addView(newTimerView,this.childCount-1)
        }
    }
}