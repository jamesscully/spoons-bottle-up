package com.scullyapps.spoonsbottleup.models

import android.database.Cursor

data class Bottle(
        val id: String = "",
        var name: String,
        var step: Int = 2,
        var max: Int = -1,
        var fridgeName: String? = "Default Fridge",
        var listOrder: Int = -1
) {

    var custom : Boolean = false

    constructor(name : String, step : Int = 2, max : Int = -1, fName : String = "Default")
            : this("", name, step, max, fName)
    {
        custom = true
    }

    constructor() : this("Error", 0, -1)

    companion object {
        fun fromCursor(cursor : Cursor) : Bottle {
            val id = cursor.getString(0)
            val name = cursor.getString(1)
            val order = cursor.getInt(2)
            val step = cursor.getInt(3)
            val max = cursor.getInt(4)
            val fridge = cursor.getString(5) ?: "Default"
            return Bottle(id, name, step, max, fridge, order)
        }
    }
}