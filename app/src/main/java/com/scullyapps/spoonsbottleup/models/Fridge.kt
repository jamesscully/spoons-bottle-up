package com.scullyapps.spoonsbottleup.models

class Fridge(
        val id : Int,
        var name : String,
        val bottles : List<Bottle>,
        val listOrder : Int = 0
) {
    private val TAG : String = "Fridge"

}