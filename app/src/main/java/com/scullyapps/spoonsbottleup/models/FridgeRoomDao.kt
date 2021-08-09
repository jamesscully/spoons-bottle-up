package com.scullyapps.spoonsbottleup.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FridgeRoomDao {
    @Insert
    fun insert(fridge: Fridge)

    @Insert
    fun insert(vararg fridge: Fridge)

    @Update
    fun update(fridge: Fridge)

    @Update
    fun update(vararg fridge: Fridge)

    @Query("SELECT * FROM Fridges ORDER BY ListOrder ASC")
    fun getAll() : List<Fridge>

    @Query("SELECT * FROM Fridges WHERE name = :name")
    fun query(name : String) : Fridge
}