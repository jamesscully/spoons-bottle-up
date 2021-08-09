package com.scullyapps.spoonsbottleup.data

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scullyapps.spoonsbottleup.models.BottleRoom
import com.scullyapps.spoonsbottleup.models.BottleRoomDao
import com.scullyapps.spoonsbottleup.models.FridgeRoom
import com.scullyapps.spoonsbottleup.models.FridgeRoomDao

@Database(entities = [BottleRoom::class, FridgeRoom::class], version = 1, exportSchema = true)
abstract class BottleRoomDatabase : RoomDatabase() {
    abstract val bottleRoomDao: BottleRoomDao
    abstract val fridgeRoomDao: FridgeRoomDao

    companion object {
        private var INSTANCE: BottleRoomDatabase? = null

        fun getInstance(context: Context): BottleRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            BottleRoomDatabase::class.java,
                            "BottleDatabase.db")
                            .createFromAsset("Bottles.db")
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}