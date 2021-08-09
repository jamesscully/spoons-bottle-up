package com.scullyapps.spoonsbottleup.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "Fridges")
data class FridgeRoom(
        @PrimaryKey
        val id: Int,

        @ColumnInfo(name = "Name")
        var name: String,

        @ColumnInfo(name = "ListOrder")
        var listOrder: Int
        ) {
}