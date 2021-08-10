package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import kotlinx.android.synthetic.main.widget_fridge.view.*
import kotlinx.android.synthetic.main.widget_plaque.view.*

class FridgeView(context: Context, var fridge: Fridge) : LinearLayout(context) {

    var bottles: List<Bottle>
    lateinit var database: BottleDatabase

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_fridge, this, true)
        txt_fridgename.text = fridge.name

        database = BottleDatabase.getInstance(context)

        bottles = database.bottleRoomDao.queryByFridge(fridge.name)

        for(bottle in bottles) {
            addBottle(bottle)
        }

        showAccent()
    }

    private val countBottleViews: ArrayList<CountBottleView>
        get() {
            val ret = ArrayList<CountBottleView>()

            for (i in 0 until layout_widget_fridge.childCount) {
                if (layout_widget_fridge.getChildAt(i) is CountBottleView) {
                    ret.add(layout_widget_fridge.getChildAt(i) as CountBottleView)
                }
            }

            return ret
        }

    val size: Int
        get() = bottles.size

    private fun addBottle(bottle: Bottle) {
//        layout_widget_fridge.addView(CountBottleView(context, bottle))
    }

    fun bottleUp(hide: Boolean) {
        // used to determine if we've hid anything; can hide the entire thing else
        var modified = false

        // if we're not in bottling up, then show everything
        if (!hide) {
            this.visibility = VISIBLE
            for (p in countBottleViews) {
                p.visibility = VISIBLE
                p.setInputMode(true)
            }
            return
        }

        // if we are, hide only those with amt = 0
        for (view in countBottleViews) {
            val amt = view.getCount()

            if (amt == 0 && !view.inverted) {
                view.visibility = GONE
            } else {
                // if we're here, then we've had a plaque with count > 0
                if (view.inverted) {
                    view.setCount(view.max - view.getCount())
                    view.invert(false)
                }

                view.setInputMode(false)
                modified = true
            }
        }

        if (!modified) {
            // hide all this; hides fridge headers if not needed
            this.visibility = GONE
        }
        showAccent()
    }

    fun showMaxes(locked : Boolean) {
        var visibility = View.VISIBLE

        if(!locked)
            visibility = View.INVISIBLE

        countBottleViews.forEach { view ->
            view.plaque_text_max.visibility = visibility
        }
    }


    private fun showAccent() {
        var accent = false

        for (p in countBottleViews) {
            if (p.visibility == VISIBLE) {
                if (accent)
                    p.setBackgroundResource(R.color.plaqueBackgroundAcc)
                else
                    p.setBackgroundResource(R.color.plaqueBackground)

                accent = !accent
            }
        }
    }
}