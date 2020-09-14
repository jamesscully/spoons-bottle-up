package com.scullyapps.spoonsbottleup.models


import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

// Holds information related to each pub
data class Pub(
        val name : String,
        val sortName : String,
        val id : Int,
        val topGroup : String,
        val subGroup : String,
        val hotel : Boolean,
        val airport : Boolean,
        val closed : Boolean,
        val address : Address = Address()
) {
    private val TAG : String = "Pub"

    constructor(jsonObject: JSONObject) : this(
        jsonObject.getString("name"),
        jsonObject.getString("sortName"),
        jsonObject.getInt("venueId"),
        jsonObject.getString("topGroup"),
        jsonObject.getString("subGroup"),
        jsonObject.getBoolean("isPubInHotel"),
        jsonObject.getBoolean("isAirport"),
        jsonObject.getBoolean("pubIsClosed"),
        Address(jsonObject)
    )

    override fun toString(): String {
        return "Pub $id $name"
    }
}