package com.scullyapps.spoonsbottleup.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class Bottle(
        @PrimaryKey(autoGenerate = true)
        val ID: Long,
        @ColumnInfo(name = "Name")
        var name: String,
        @ColumnInfo(name = "Description")
        var description: String? = "",
        @ColumnInfo(name = "EposName")
        var eposName: String?,
        @ColumnInfo(name = "StepAmount")
        var step: Int = 2,
        @ColumnInfo(name = "MaxAmount")
        var max: Int = -1,
        @ColumnInfo(name = "FridgeID")
        var fridgeName: String? = "Default Fridge",
        @ColumnInfo(name = "ListOrder")
        var listOrder: Int,
        @ColumnInfo(name="MinimumAge")
        var minimumAge: Int,

        @ColumnInfo(name = "IsDrink")
        var isDrink : Boolean,
        @ColumnInfo(name = "IsMisc")
        var isMisc : Boolean,
        @ColumnInfo(name = "IsDraught")
        var isDraught : Boolean,
        @ColumnInfo(name = "IsSpirit")
        var isSpirit : Boolean,
        @ColumnInfo(name = "IsCanOrBottle")
        var isCanOrBottle : Boolean,

        ) {
        override fun toString(): String {
                return "[$ID] $name belongs to $fridgeName with $max amount"
        }
}