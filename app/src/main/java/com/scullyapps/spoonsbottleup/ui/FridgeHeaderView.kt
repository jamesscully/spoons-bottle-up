package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.scullyapps.spoonsbottleup.R
import kotlinx.android.synthetic.main.widget_fridge.view.*

class FridgeHeaderView(context: Context, header: String) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_fridge, this, true)
        txt_fridgename.text = header
    }
}