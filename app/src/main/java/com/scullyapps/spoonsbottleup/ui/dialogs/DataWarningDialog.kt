package com.scullyapps.spoonsbottleup.ui.dialogs


import android.content.Context
import androidx.appcompat.app.AlertDialog

class DataWarningDialog(
        context: Context,
        title: String,
        message: String,
        negativeButton: String = "Stay",
        positiveButton: String = "Discard",
        callback: (Any, Any) -> Unit) : AlertDialog.Builder(context) {
    init {
        setTitle(title)
        setMessage(message)
        setCancelable(false)
        setNegativeButton(negativeButton) { d, _ ->
            d.cancel()
        }
        setPositiveButton(positiveButton, callback)
    }
}