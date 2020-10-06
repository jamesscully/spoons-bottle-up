package com.scullyapps.spoonsbottleup.models

import android.content.Context
import com.scullyapps.spoonsbottleup.ui.FridgeView

class Fridge(
        var name: String,
        var bottles: List<Bottle>,
        val listOrder: Int = 0
) {
    private val TAG : String = "Fridge"

    fun toView(context: Context) : FridgeView {
        return FridgeView(context, this.name)
    }
}