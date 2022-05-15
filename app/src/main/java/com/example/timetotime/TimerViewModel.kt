package com.example.timetotime

import android.util.Log
import androidx.lifecycle.ViewModel



class TimerViewModel: ViewModel() {
    // holds the init lis for all the timer seconds
    private var _timerTimeInitList: MutableList<Int> = mutableListOf(20,70,30)
    val timerTimeInitList: List<Int>
        get() = _timerTimeInitList

    // holds the init list for all the timer intervals
    private var _timerIntervalInitList:  MutableList<Int> = mutableListOf(2,1,4)
    val timerIntervalInitList: List<Int>
        get() = _timerIntervalInitList

    // which timer is active?
    private var _activeTimer = 0
    val activeTimer: Int
        get() {
            if ((_activeTimer + 1) > _timerTimeInitList.size) return 0
            return _activeTimer
        }

    // is the timer running?
    var timerStarted = false
    var timerCompleted = false

    private var totalSeconds: Int = 0



    private val activeTimerSecondsTotal: Int
        get() = _timerTimeInitList[_activeTimer]

    var activeTimerSecondsRemaining: Int = activeTimerSecondsTotal
        get() {
            return if (field < 0) 0
            else field
        }


    val activeTimerTimeElapsed: Int
        get() = activeTimerSecondsTotal-activeTimerSecondsRemaining

    private var _activeIntervalsRemaining: Int = _timerIntervalInitList[activeTimer]

    val activeIntervalsRemaining: Int
        get() = _activeIntervalsRemaining

    // adds the new timer values into the init list. If one of the time values isn't passed it'll just use 0
    fun addNewTimer(newTimeMin: Int? = 0, newTimeSec: Int? = 0, newInterval: Int? = 1) {
        val timeInSeconds = (newTimeMin ?: 0) * 60 + (newTimeSec ?: 0)
        _timerTimeInitList.add(timeInSeconds)
        _timerIntervalInitList.add(newInterval ?: 1)
        recalculateTotalTime()
    }

    private fun recalculateTotalTime() {
        var total = 0
        for (i in 0 until _timerTimeInitList.size) {
            total += _timerTimeInitList[i] * _timerIntervalInitList[i]
        }
        totalSeconds = total
    }
    private fun setActiveTimerAfterBreak(time: Int) {
        var tempTime = 0
        for (i in 0 until _timerTimeInitList.size) {
            for (j in 0 until _timerIntervalInitList.size) {
                tempTime += _timerTimeInitList[i]
                if (tempTime >= time) {
                    activeTimerSecondsRemaining = _timerTimeInitList[i]-(tempTime-time)
                    _activeTimer = i
                    _activeIntervalsRemaining = _timerIntervalInitList[i]-j
                }
            }
        }
    }

    fun resetAllTimersToInit() {
        _activeTimer = 0
        timerCompleted = false
        timerStarted = false
        activeTimerSecondsRemaining = activeTimerSecondsTotal
        _activeIntervalsRemaining = _timerIntervalInitList[activeTimer]
        timerCompleted = false
    }

    fun checkIfTimerFinished() : Boolean{
        if (activeTimerSecondsRemaining <=0) {

            Log.d("TimerViewModel", "Seconds: $activeTimerSecondsRemaining, intervals: $activeIntervalsRemaining, timer: $activeTimer")
            if (activeIntervalsRemaining <= 0) {
                Log.d("TimerViewModel","nextTimer")
                if (checkIfLastTimer()) {
                    Log.d("TimerViewModel","Timer Completed")
                    timerCompleted = true
                    timerStarted = false
                    return true
                }
                nextTimer()
            }
            --_activeIntervalsRemaining
            activeTimerSecondsRemaining = activeTimerSecondsTotal

        }
        return false
    }

    //checks if we hit the last timer
    fun checkIfLastTimer() : Boolean {
        val last: Boolean = _activeTimer >= _timerTimeInitList.size-1
        return last
    }

    // advances the timer
    private fun nextTimer() {
        if(!checkIfLastTimer()) {
            Log.d("TimerViewModel","Advancing")
            ++_activeTimer
            _activeIntervalsRemaining = timerIntervalInitList[activeTimer]
        }
    }
}