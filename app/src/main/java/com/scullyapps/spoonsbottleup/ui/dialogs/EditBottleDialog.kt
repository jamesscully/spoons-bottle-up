package com.scullyapps.spoonsbottleup.ui.dialogs


import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import kotlinx.android.synthetic.main.dialog_edit_bottle.view.*

class EditBottleDialog(context: Context, bottle: Bottle) : AlertDialog.Builder(context) {
    private val TAG: String = "EditBottleDialog"

    init {

        val inflater = LayoutInflater.from(context).inflate(R.layout.dialog_edit_bottle, null)

        inflater.eb_bottlename.text = bottle.name
        inflater.eb_max.setText(bottle.max)
        inflater.eb_step.setText(bottle.step)

        setTitle("Editing ${bottle.name}")
        setView(inflater)
    }
}