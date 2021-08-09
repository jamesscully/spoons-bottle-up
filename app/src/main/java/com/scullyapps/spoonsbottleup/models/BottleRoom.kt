package com.scullyapps.spoonsbottleup.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bottles")
data class BottleRoom(
        @PrimaryKey
        val id: String = "",
        @ColumnInfo(name = "Name")
        var name: String,
        @ColumnInfo(name = "StepAmount")
        var step: Int = 2,
        @ColumnInfo(name = "MaxAmount")
        var max: Int = -1,
        @ColumnInfo(name = "FridgeID")
        var fridgeName: String? = "Default Fridge",
        @ColumnInfo(name = "ListOrder")
        var listOrder: Int = -1
) {
}