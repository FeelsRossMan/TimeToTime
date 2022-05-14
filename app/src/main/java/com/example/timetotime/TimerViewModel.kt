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



    fun addNewTimer(newTime: Int, newInterval: Int) {
        _timerTimeInitList.add(newTime)
        _timerIntervalInitList.add(newInterval)
    }


    // Converts from a time int in milliseconds to a time string


}