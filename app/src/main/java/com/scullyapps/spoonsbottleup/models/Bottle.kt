package com.scullyapps.spoonsbottleup.models


import android.util.Log
import com.scullyapps.spoonsbottleup.DrinkType

data class Bottle(
        val id : Int,
        val name : String,
        val type : DrinkType,
        var step : Int = 2,
        var max  : Int = 32,
        var fridgeName : String = "Default Fridge",
        var listOrder : Int = -1
) {
    private val TAG : String = "Bottle"

    var custom : Boolean = false

    constructor(name : String, step : Int = 2, max : Int = 32, fName : String = "Default")
            : this(-1, name, DrinkType.CUSTOM, step, max, fName)
    {
        custom = true
    }

    constructor() : this("Error", 0, 1)

    companion object {
        fun fromJson() {

        }
    }
}