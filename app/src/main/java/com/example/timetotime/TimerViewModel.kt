package com.example.timetotime

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private var _timerTimeInitList: MutableLiveData<List<Int>> = MutableLiveData(listOf(0))
    private var _timerIntervalInitList: MutableLiveData<List<Int>> = MutableLiveData(listOf(0))

    val timerTimeInitList = _timerTimeInitList
    val timerIntervalInitList = _timerTimeInitList

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
    fun addASecondToAll() {
        val newList = mutableListOf<Int>(0)
        _timerTimeInitList.value!!.forEach { time ->
            Log.d("TimerViewModel", "Current time: ${time}")
            var tmp = time + 1
            newList.plus(tmp)
        }
        Log.d("TimerViewModel", "${_timerTimeInitList.value!!.size}")
        _timerTimeInitList.value = newList.toList()
        Log.d("TimerViewModel", "${_timerTimeInitList.value!!.size}")
    }

}