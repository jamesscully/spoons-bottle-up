package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.BottleRoom
import kotlinx.android.synthetic.main.widget_plaque.view.*

class CountBottleView : LinearLayout {

    private var count = 0

    @JvmField
    var inverted = false

    var bottle: BottleRoom

    var normalBgId = 0

    constructor(context: Context, bottle: BottleRoom) : super(context) {
        this.bottle = bottle
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.widget_plaque, this, true)
        plaque_text_name.text = bottle.name

        // only show if it has a max value, and we're showing maxes
        if(max >= 0 && showMaxes)
            plaque_text_max.text = String.format("/%3d", max)

        setupButtons()


        val invertListener = View.OnLongClickListener { _: View? ->
            if(allowInvert && count <= max && max >= 0)
                invert()
            true
        }

        // set both our text and body to respond to long presses
        setOnLongClickListener(invertListener)
        plaque_text_name.setOnLongClickListener(invertListener)

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
            addToTotal(1)
        } else if (count + amt > max) {
            // if we're above the max, but the max is 0 or we have unlocked maxes,
            // then let the user input as many as they want
            // (in the case of missing data)
            if ((max <= 0 || !lockMaxes) || !showMaxes) {
                count += amt
                addToTotal(amt)
            } else {
                // else, set to max
                count = max
            }
        }
        else  {
            count += amt
            addToTotal(amt)
        }

        plaque_text_count!!.text = count.toString()
    }

    private fun decrement(amt: Int) {
        Log.w(" [ Plaque fridge: $name ]", "Removing $amt from current count: $count")

        if (count - amt < 0) {
            count = 0
        } else {
            count -= amt
            addToTotal(-amt)
        }

        plaque_text_count!!.text = count.toString()
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
        plaque_text_count!!.text = count.toString()
        this.count = count
    }

    val name: String
        get() = bottle.name
    val max: Int
        get() = bottle.max

    companion object {
        var totalSelected = MutableLiveData(0)

        var allowInvert : Boolean = true
        var showMaxes : Boolean = true
        var lockMaxes : Boolean = true

        fun addToTotal(amt : Int) {
            // value should never be null, but 0 if somehow not
            val value = totalSelected.value ?: 0

            totalSelected.postValue(value + amt)
        }
    }
}