package com.scullyapps.spoonsbottleup.ui.dialogs


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import kotlinx.android.synthetic.main.dialog_edit_bottle.view.*

class EditBottleDialog(context: Context, bottle: Bottle) : AlertDialog.Builder(context) {

    // used to handle changes outside of this dialog, when we've submitted info
    var onSubmitted : ((Bottle) -> Unit) = {}

    init {
        val inflater = LayoutInflater.from(context).inflate(R.layout.dialog_edit_bottle, null)

        val name   = inflater.eb_bottlename
        val max    = inflater.eb_max
        val step   = inflater.eb_step
        val fridge = inflater.eb_fridge

        setTitle("Editing ${bottle.name}")

        name.text = bottle.name
        max.setText("${bottle.max}")
        step.setText("${bottle.step}")

        val fridgeNames = BottleDatabase.FridgeUtils.getNames()

        fridge.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, fridgeNames)
        fridge.setSelection(fridgeNames.indexOf(bottle.fridgeName))

        setPositiveButton("Update") { dialog, _ ->
            bottle.max = max.text.toString().toInt()
            bottle.step = step.text.toString().toInt()

            bottle.fridgeName = fridge.selectedItem as String

            BottleDatabase.BottleUtils.update(bottle.id, bottle)

            onSubmitted(bottle)

            dialog.cancel()
        }

        setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        setView(inflater)
    }
}