package com.scullyapps.spoonsbottleup.models

import android.content.Context
import com.scullyapps.spoonsbottleup.ui.FridgeView

class Fridge(
        var name: String,
        var bottles: List<Bottle>,
        var listOrder: Int = 0
) {

    fun toView(context: Context) : FridgeView {
        return FridgeView(context, this)
    }

    operator fun component1() {
        TODO("Not yet implemented")
    }
}