package com.scullyapps.spoonsbottleup.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FridgeRoomDao {
    @Insert
    fun insert(fridge: Fridge)

    @Update
    fun update(fridge: Fridge)
}