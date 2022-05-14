package com.example.timetotime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private var _timerTimeInitList: MutableList<Int> = mutableListOf(0)
    val timerTimeInitList: List<Int>
        get() = _timerTimeInitList

    private var _timerIntervalInitList:  MutableList<Int> = mutableListOf(0)
    val timerIntervalInitList: List<Int>
        get() = _timerIntervalInitList



    fun addNewTimer(newTimeMin: Int? = 0, newTimeSec: Int? = 0, newInterval: Int = 0) {
        val timeInSeconds = (newTimeMin ?: 0) * 60 + (newTimeSec ?: 0)
        _timerTimeInitList.add(timeInSeconds)
        _timerIntervalInitList.add(newInterval)
    }


    // Converts from a time int in milliseconds to a time string


}