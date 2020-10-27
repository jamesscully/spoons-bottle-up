package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.DrinkType
import kotlinx.android.synthetic.main.widget_plaque.view.*

class CountBottleView : LinearLayout {

    private var count = 0

    @JvmField
    var inverted = false

    var bottle: Bottle

    var normalBgId = 0

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

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.widget_plaque, this, true)
        plaque_text_name.text = bottle.name

        if(max >= 0)
            plaque_text_max.text = String.format("/%3d", max)

        setupButtons()

        setOnLongClickListener {
            invert()
            true
        }

        plaque_text_name.setOnLongClickListener {
            invert()
            true
        }

        // show bottle name on click
        plaque_text_name.setOnClickListener {
            Toast.makeText(context, bottle.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        plaque_button_inc.setOnClickListener { increment(1) }
        plaque_button_dec.setOnClickListener { decrement(1) }
        plaque_button_stepdec.setOnClickListener { decrement(bottle.step) }
        plaque_button_stepinc.setOnClickListener { increment(bottle.step) }
    }

    private fun increment(amt: Int) {
        Log.w(" [ Plaque fridge: $name ]", "Adding $amt to current count: $count")

        // if for some reason we're incrementing <= 0, just add 1
        if (amt <= 0) {
            count++
            totalSelected++
        }
        else if (count + amt > max)
            count = max
        else  {
            count += amt
            totalSelected += amt
        }

        plaque_text_count!!.text = Integer.toString(count)
    }

    private fun decrement(amt: Int) {
        Log.w(" [ Plaque fridge: $name ]", "Removing $amt from current count: $count")

        if (count - amt < 0) {
            count = 0
        } else {
            count -= amt
            totalSelected -= amt
        }

        plaque_text_count!!.text = Integer.toString(count)
    }

    fun invert() {
        if (!inverted) {
            // use super class to temporarily modify color
            super.setBackgroundResource(R.color.countInvertedColour)
            plaque_text_name.setTextColor(Color.WHITE)
            plaque_text_count.setTextColor(Color.WHITE)
            plaque_text_max.visibility = INVISIBLE
        } else {

            // use our version to revert to accentized colour
            setBackgroundResource(normalBgId)
            plaque_text_name.setTextColor(Color.parseColor("#8A000000"))
            plaque_text_count.setTextColor(Color.parseColor("#8A000000"))
            plaque_text_max.visibility = VISIBLE
        }
        inverted = !inverted
    }

    fun invert(b: Boolean) {
        inverted = !b
        invert()
    }

    fun setInputMode(mode: Boolean) {
        plaque_button_stepdec.visible(mode)
        plaque_button_dec.visible(mode)
        plaque_button_stepinc.visible(mode)
        plaque_button_inc.visible(mode)
    }

    private fun View.visible(bool : Boolean) {
        if(bool)
            this.visibility = VISIBLE
        else
            this.visibility = GONE
    }

    override fun setBackgroundResource(resID: Int) {
        super.setBackgroundResource(resID)
        normalBgId = resID
    }

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        plaque_text_count!!.text = Integer.toString(count)
        this.count = count
    }

    val name: String
        get() = bottle.name
    val max: Int
        get() = bottle.max
    val type: DrinkType
        get() = bottle.type

    companion object {
        var totalSelected = 0
    }
}