package com.example.timetotime.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetotime.R

class TimerAdapter(context: Context) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {
    class TimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view)  {
        val timerText: EditText = view.findViewById(R.id.running_timer_text)
        val repeatTimerText: EditText = view.findViewById(R.id.running_timer_repeats_text)
        val imageView: ImageView = view.findViewById(R.id.card_move_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}