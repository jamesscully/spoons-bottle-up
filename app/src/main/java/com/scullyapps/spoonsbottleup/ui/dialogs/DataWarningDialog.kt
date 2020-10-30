package com.scullyapps.spoonsbottleup.ui.dialogs


import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class DataWarningDialog(
        context: Context,
        title : String,
        message : String,
        negativeButton : String = "Stay",
        positiveButton : String = "Discard",
        callback : DialogInterface.OnClickListener) : AlertDialog.Builder(context) {
    init {
        setTitle(title)
        setMessage(message)
        setCancelable(false)
        setNegativeButton(negativeButton) { d, i ->
            d.cancel()
        }
        setPositiveButton(positiveButton, callback)
    }
}