package com.example.timetotime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private var _timerTimeInitList: MutableLiveData<List<Int>> = MutableLiveData(listOf(0))
    private var _timerIntervalInitList: MutableLiveData<List<Int>> = MutableLiveData(listOf(0))

    val timerTimeInitList: LiveData<List<Int>> = _timerTimeInitList
    val timerIntervalInitList: LiveData<List<Int>> = _timerTimeInitList

    fun addNewTimer(newTime: Int, newInterval: Int) {
        _timerTimeInitList.value = _timerTimeInitList.value?.plus(newTime) ?:  listOf(newTime)
        _timerIntervalInitList.value = _timerIntervalInitList.value?.plus(newTime) ?: listOf(newInterval)
    }


    // Converts from a time int in milliseconds to a time string
    private fun getTimeStringFromDouble(time: Int) : String{
        val resultInt = time
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        return makeTimeString(hours, minutes, seconds)
    }

}