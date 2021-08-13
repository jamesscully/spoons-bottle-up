package com.scullyapps.spoonsbottleup.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.BottleRoomDao
import com.scullyapps.spoonsbottleup.models.Fridge
import com.scullyapps.spoonsbottleup.models.FridgeRoomDao

@Database(
        entities = [Bottle::class, Fridge::class],
        version = 1,
        exportSchema = true
)
abstract class BottleDatabase : RoomDatabase() {
    abstract val bottleRoomDao: BottleRoomDao
    abstract val fridgeRoomDao: FridgeRoomDao

    companion object {
        private var INSTANCE: BottleDatabase? = null

        fun getInstance(context: Context): BottleDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            BottleDatabase::class.java,
                            "BottleDatabase.db")
                            .createFromAsset("Bottles.db")
                            .allowMainThreadQueries()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

