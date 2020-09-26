package com.scullyapps.spoonsbottleup.models

import android.database.Cursor


data class Bottle(
        val id : Int,
        var name : String,
        var type : DrinkType,
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

        fun fromCursor(cursor : Cursor) : Bottle {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val order = cursor.getInt(2)
            val step = cursor.getInt(3)
            val max = cursor.getInt(4)
            val fridge = cursor.getString(5) ?: "Default"
            return Bottle(id, name, DrinkType.DUMMY, step, max, fridge, order)
        }
    }

    object SQL {
        val ID = "ID"
        val NAME = "Name"
        val LIST_ORDER = "ListOrder"
        val STEP = "StepAmount"
        val MAX = "MaxAmount"
        val FRIDGE = "FridgeID"
    }
}