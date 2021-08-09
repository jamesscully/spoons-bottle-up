package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Entity(tableName = "Fridges")
data class FridgeRoom(
        @PrimaryKey(autoGenerate = true)
        var id: Int,

        @ColumnInfo(name = "Name")
        var name: String,

        @ColumnInfo(name = "ListOrder")
        var listOrder: Int,
        ) {
}