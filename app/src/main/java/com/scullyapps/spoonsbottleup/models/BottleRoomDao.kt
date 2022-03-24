package com.scullyapps.spoonsbottleup.models

import androidx.room.*

@Dao
interface BottleRoomDao {

    @Insert
    fun insert(vararg bottle: Bottle)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg bottle: Bottle)

    @Delete
    fun delete(bottle: Bottle)

    @Query("SELECT * FROM Product")
    fun getAll() : List<Bottle>?

    @Query("SELECT * FROM Product WHERE id = :key")
    fun query(key: Long) : Bottle?

    @Query("SELECT * FROM Product WHERE Name = :name LIMIT 1")
    fun queryByName(name: String) : Bottle?

    @Query("SELECT * FROM Product WHERE IsCanOrBottle = 1")
    fun getAllBottles() : List<Bottle>?

    @Query("SELECT * FROM Product WHERE IsDraught = 1")
    fun getAllDraught() : List<Bottle>?

    @Query("SELECT * FROM Product WHERE IsSpirit = 1")
    fun getAllSpirit() : List<Bottle>?

    @Query("SELECT * FROM Product WHERE FridgeID = :fridgeName AND IsCanOrBottle = 1 ORDER BY ListOrder ASC")
    fun queryBottlesByFridge(fridgeName: String) : List<Bottle>

    @Query("SELECT * FROM Product WHERE FridgeID = '' AND IsCanOrBottle = 1")
    fun getUndefinedBottles() : List<Bottle>
}