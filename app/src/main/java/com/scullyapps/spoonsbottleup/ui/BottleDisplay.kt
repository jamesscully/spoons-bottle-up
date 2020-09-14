package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import kotlinx.android.synthetic.main.fragment_bottle_display.view.*

class BottleDisplay : ConstraintLayout {
    private var bottle: Bottle = Bottle()


    constructor(context: Context, bottle: Bottle) : super(context) {
        this.bottle = bottle
    }

    constructor(context: Context, attrs: AttributeSet?, bottle: Bottle) : super(context, attrs) {
        this.bottle = bottle
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, bottle: Bottle) : super(context, attrs, defStyleAttr) {
        this.bottle = bottle
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_bottle_display, this, true)

        bottle_display_text_name.text = bottle.name
        bottle_display_text_fridge.text = "Max: " + bottle.max
    }
}