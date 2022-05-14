package com.example.timetotime

import android.util.Log
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    // holds the init lis for all the timer seconds
    private var _timerTimeInitList: MutableList<Int> = mutableListOf(300)
    val timerTimeInitList: List<Int>
        get() = _timerTimeInitList

    val totalSeconds: Int
        get() {
            var totalTime = 0
            for (time in _timerTimeInitList) totalTime+=time
            return totalTime
        }

    val activeTimerSeconds: Int
        get() = _timerTimeInitList[_activeTimer]

    // holds the init list for all the timer intervals
    private var _timerIntervalInitList:  MutableList<Int> = mutableListOf(0)
    val timerIntervalInitList: List<Int>
        get() = _timerIntervalInitList

    // which timer is active?
    private var _activeTimer = 1

    val activeTimer: Int
        get() = _activeTimer



    // adds the new timer values into the init list. If one of the time values isn't passed it'll just use 0
    fun addNewTimer(newTimeMin: Int? = 0, newTimeSec: Int? = 0, newInterval: Int? = 1) {
        val timeInSeconds = (newTimeMin ?: 0) * 60 + (newTimeSec ?: 0)
        _timerTimeInitList.add(timeInSeconds)
        _timerIntervalInitList.add(newInterval ?: 1)
    }
}