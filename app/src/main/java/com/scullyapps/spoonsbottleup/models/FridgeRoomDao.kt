package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Dao
interface FridgeRoomDao {
    @Insert
    fun insert(fridge: Fridge)

    @Insert
    fun insert(vararg fridge: Fridge)

    @Delete
    fun delete(fridge: Fridge)

    @Update
    fun update(fridge: Fridge)

    @Update
    fun update(vararg fridge: Fridge)

    @Query("SELECT * FROM Fridges ORDER BY ListOrder ASC")
    fun getAll() : List<Fridge>

    @Query("SELECT * FROM Fridges WHERE name = :name")
    fun query(name : String) : Fridge
}