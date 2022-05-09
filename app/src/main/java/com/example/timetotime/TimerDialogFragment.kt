package com.example.timetotime

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class TimerDialogFragment : DialogFragment() {

    companion object {
        const val Tag = "NewTimerDialogBox"
    }

    //TODO: Set it on up
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Enter a time")
                .setPositiveButton("Set", DialogInterface.OnClickListener{dialog, id -> })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{dialog, id ->})
            builder.create()
        } ?: throw IllegalStateException("activity cannot be null")
    }
}
