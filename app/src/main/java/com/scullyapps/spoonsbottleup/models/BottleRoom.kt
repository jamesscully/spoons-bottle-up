package com.scullyapps.spoonsbottleup.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bottles")
data class BottleRoom(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        @ColumnInfo(name = "Name")
        var name: String,
        @ColumnInfo(name = "StepAmount")
        var step: Int = 2,
        @ColumnInfo(name = "MaxAmount")
        var max: Int = -1,
        @ColumnInfo(name = "FridgeID")
        var fridgeName: String? = "Default Fridge",
        @ColumnInfo(name = "ListOrder")
        var listOrder: Int = -1,

        @ColumnInfo(name="MinimumAge")
        var minimumAge: Int,
        @ColumnInfo(name = "SizeML" )
        var sizeMl: Int = 0

) {
        override fun toString(): String {
                return "[$id] $name belongs to $fridgeName with $max amount"
        }
}