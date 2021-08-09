package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Dao
interface BottleRoomDao {
    @Insert
    fun insert(bottle: BottleRoom)

    @Update
    fun update(bottle: BottleRoom)

    @Query("SELECT * FROM Bottles WHERE ID = :key")
    fun query(key: Long) : BottleRoom?

    @Query("SELECT * FROM Bottles WHERE Name = :name LIMIT 1")
    fun query(name: String)
}