package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import kotlinx.android.synthetic.main.widget_bottle_list.view.*

class BottleDisplay : ConstraintLayout {
    private var bottle: Bottle


    constructor(context: Context, bottle: Bottle) : super(context) {
        this.bottle = bottle
        init()
    }

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.widget_bottle_list, this, true)

        bottle_display_text_name.text = bottle.name
        bottle_display_text_fridge.text = "Max: $bottle.max"
    }
}