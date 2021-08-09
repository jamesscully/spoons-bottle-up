package com.scullyapps.spoonsbottleup

import android.util.Log
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.scullyapps.spoonsbottleup.data.BottleRoomDatabase
import com.scullyapps.spoonsbottleup.models.BottleRoom
import com.scullyapps.spoonsbottleup.models.BottleRoomDao
import com.scullyapps.spoonsbottleup.models.FridgeRoom
import com.scullyapps.spoonsbottleup.models.FridgeRoomDao
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class BottleRoomDatabaseTest {

    private lateinit var bottlesDao: BottleRoomDao
    private lateinit var fridgesDao: FridgeRoomDao
    private lateinit var database: BottleRoomDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(
                    context,
                    BottleRoomDatabase::class.java
                )
                .allowMainThreadQueries()
                .build()

        bottlesDao = database.bottleRoomDao
        fridgesDao = database.fridgeRoomDao
    }

    @After
    @Throws(IOException::class)
    fun clean() {
        database.close()
    }



    @Test
    fun insertFridgesTest() {
        val fridge1 = FridgeRoom(1,"Test1", 5)
        val fridge2 = FridgeRoom(2,"Test2", 4)
        val fridge3 = FridgeRoom(3,"Test3", 98)

        fridgesDao.insert(fridge1)
        fridgesDao.insert(fridge2)
        fridgesDao.insert(fridge3)

        assertTrue(fridgesDao.query("Test1").listOrder == 5)
        assertTrue(fridgesDao.query("Test2").listOrder == 4)
        assertTrue(fridgesDao.query("Test3").listOrder == 98)
    }

    @Test
    fun updateFridgesTest() {
        val fridge1 = FridgeRoom(1,"Test1", 5)
        val fridge2 = FridgeRoom(2,"Test2", 4)
        val fridge3 = FridgeRoom(3,"Test3", 98)

        fridgesDao.insert(fridge1, fridge2, fridge3)

        fridge1.listOrder = 1
        fridge2.listOrder = 2
        fridge3.listOrder = 3

        fridge3.name = "Hehe"

        fridgesDao.update(fridge1, fridge2, fridge3)

        assertTrue(fridgesDao.query("Test1").listOrder == 1)
        assertTrue(fridgesDao.query("Test2").listOrder == 2)
        assertTrue(fridgesDao.query("Hehe").listOrder == 3)
    }
}