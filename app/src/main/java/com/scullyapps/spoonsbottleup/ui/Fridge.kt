package com.scullyapps.spoonsbottleup.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import kotlinx.android.synthetic.main.widget_fridge.view.*
import java.util.*
import kotlin.collections.ArrayList

class Fridge(context: Context, var name: String) : LinearLayout(context) {

    var bottles: ArrayList<Bottle> = ArrayList()
        set(value) {
            value.forEach {bottle ->
                addBottle(bottle)
                accentize()
            }
            field = value
        }

    private val plaques: ArrayList<Plaque>
        get() {
            val ret = ArrayList<Plaque>()
            for (i in 0 until layout_widget_fridge.childCount) {
                if (layout_widget_fridge.getChildAt(i) is Plaque) {
                    ret.add(layout_widget_fridge.getChildAt(i) as Plaque)
                }
            }
            return ret
        }

    val size: Int
        get() = bottles.size

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_fridge, this, true)
        txt_fridgename.text = name
    }

    fun addBottle(bottle: Bottle) {
        bottles.add(bottle)
        layout_widget_fridge.addView(Plaque(context, bottle))
    }

    fun bottleUp(hide: Boolean) {
        // used to determine if we've hid anything; can hide the entire thing else
        var modified = false

        // if we're not in bottling up, then show everything
        if (!hide) {
            this.visibility = VISIBLE
            for (p in plaques) {
                p.visibility = VISIBLE
                p.setInputMode(true)
            }
            return
        }

        // if we are, hide only those with amt = 0
        for (p in plaques) {
            val amt = p.getCount()

            if (amt == 0) {
                p.visibility = GONE
            } else {
                // if we're here, then we've had a plaque with count > 0
                if (p.inverted) {
                    p.setCount(p.max - p.getCount())
                    p.invert(false)
                }

                p.setInputMode(false)
                modified = true
            }
        }

        if (!modified) {
            // hide all this; hides fridge headers if not needed
            this.visibility = GONE
        }
        accentize()
    }

    private fun accentize() {
        var accent = false
        for (p in plaques) {
            if (p.visibility == VISIBLE) {
                if (accent)
                    p.setBackgroundResource(R.color.plaqueBackgroundAcc)
                else
                    p.setBackgroundResource(R.color.plaqueBackground)

                accent = !accent
            }
        }
    }

    object SQL {
        val NAME = "Name"
        val LIST_ORDER = "ListOrder"
    }


}