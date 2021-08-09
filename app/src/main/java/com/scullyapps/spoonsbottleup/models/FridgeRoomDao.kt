package com.scullyapps.spoonsbottleup.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeRoomDao {
    @Insert
    fun insert(fridge: FridgeRoom)

    @Insert
    fun insert(vararg fridge: FridgeRoom)

    @Update
    fun update(fridge: FridgeRoom)

    @Update
    fun update(vararg fridge: FridgeRoom)

    @Query("SELECT * FROM Fridges ORDER BY ListOrder ASC")
    fun getAll() : List<FridgeRoom>

    @Query("SELECT * FROM Fridges WHERE name = :name")
    fun query(name : String) : FridgeRoom
}