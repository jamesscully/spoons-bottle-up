package com.scullyapps.spoonsbottleup.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Fridges")
data class FridgeRoom(
        @PrimaryKey
        var name: String,
//        var bottles: List<Bottle>,
        @ColumnInfo(name = "ListOrder")
        var listOrder: Int = 0) {

}