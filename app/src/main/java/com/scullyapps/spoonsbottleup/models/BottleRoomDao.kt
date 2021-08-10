package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Dao
interface BottleRoomDao {
    @Insert
    fun insert(bottle: Bottle)

    @Update
    fun update(bottle: Bottle)

    @Delete
    fun delete(bottle: Bottle)

    @Query("SELECT * FROM Bottles")
    fun getAll() : List<Bottle>?

    @Query("SELECT * FROM Bottles WHERE id = :key")
    fun query(key: Long) : Bottle?

    @Query("SELECT * FROM Bottles WHERE Name = :name LIMIT 1")
    fun queryByName(name: String) : Bottle?

    @Query("SELECT * FROM Bottles WHERE FridgeID = :fridgeName ORDER BY ListOrder ASC")
    fun queryByFridge(fridgeName: String) : List<Bottle>
}