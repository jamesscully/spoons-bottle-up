package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Entity(tableName = "Fridges")
data class Fridge(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "Name")
        var name: String,

        @ColumnInfo(name = "ListOrder")
        var listOrder: Int,
        ) {
}