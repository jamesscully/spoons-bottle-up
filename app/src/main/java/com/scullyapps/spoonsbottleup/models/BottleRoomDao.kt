package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Dao
interface BottleRoomDao {
    @Insert
    fun insert(bottle: BottleRoom)

    @Update
    fun update(bottle: BottleRoom)

    @Query("SELECT * FROM Bottles")
    fun getAll() : List<BottleRoom>?

    @Query("SELECT * FROM Bottles WHERE id = :key")
    fun query(key: Long) : BottleRoom?

    @Query("SELECT * FROM Bottles WHERE Name = :name LIMIT 1")
    fun queryByName(name: String) : BottleRoom?

    @Query("SELECT * FROM Bottles WHERE FridgeID = :fridgeName ORDER BY ListOrder ASC")
    fun queryByFridge(fridgeName: String) : List<BottleRoom>
}