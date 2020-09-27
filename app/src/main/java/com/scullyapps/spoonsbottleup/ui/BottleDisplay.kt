package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import kotlinx.android.synthetic.main.widget_bottle_list.view.*

class BottleDisplay : ConstraintLayout {
    private var bottle: Bottle = Bottle()


    constructor(context: Context, bottle: Bottle) : super(context) {
        this.bottle = bottle
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, bottle: Bottle) : super(context, attrs) {
        this.bottle = bottle
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, bottle: Bottle) : super(context, attrs, defStyleAttr) {
        this.bottle = bottle
        init()
    }

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.widget_bottle_list, this, true)

        bottle_display_text_name.text = bottle.name
        bottle_display_text_fridge.text = "Max: $bottle.max"
        bottle_display_text_type.text = bottle.type.name
    }
}