package com.scullyapps.spoonsbottleup.models

import android.util.Log

class Fridge(
        val id : Int,
        var name : String,
        val bottles : List<Bottle>
) {
    private val TAG : String = "Fridge"

    val size
        get() = bottles.size

    fun update() {
        // todo write bottles + name to db
    }
}