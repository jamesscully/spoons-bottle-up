package com.scullyapps.spoonsbottleup.models


import org.json.JSONObject

// encapsulate address details - can be used for maps potentially
data class Address(
        val lat : Double,
        val lng : Double,
        val country : String,
        val county : String,
        val town : String,
        val postcode : String,
        val closed : Boolean,
        val menuLocation : String
) {
    private val TAG: String = "Address"

    constructor() : this (0.0, 0.0, "Default Country", "Default County", "Default Town", "Default Postcode", true, "Default")

    constructor(jsonObject: JSONObject) : this(
        jsonObject.getDouble("lat"),
        jsonObject.getDouble("long"),
        jsonObject.getString("country"),
        jsonObject.getString("county"),
        jsonObject.getString("town"),
        jsonObject.getString("postcode"),
        jsonObject.getBoolean("pubIsClosed"),
        jsonObject.getString("menuLocation")
    )


}