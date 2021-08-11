package com.scullyapps.spoonsbottleup

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class BottleDatabaseTest {

    private lateinit var bottlesDao: BottleRoomDao
    private lateinit var fridgesDao: FridgeRoomDao
    private lateinit var database: BottleDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().context

        database = Room.inMemoryDatabaseBuilder(
                    context,
                    BottleDatabase::class.java
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
        val fridge1 = Fridge(1,"Test1", 5)
        val fridge2 = Fridge(2,"Test2", 4)
        val fridge3 = Fridge(3,"Test3", 98)

        val bottle1 = Bottle(5, "Testers", 2, -1, "Test1", -1, 18, 0)

        var output : List<Bottle>

        runBlocking {
            bottlesDao.insert(bottle1)

            fridgesDao.insert(fridge1)
            fridgesDao.insert(fridge2)
            fridgesDao.insert(fridge3)

            output = bottlesDao.queryByFridge("Test1")
        }

        System.out.println(output)

        runBlocking {
            assertTrue(fridgesDao.query("Test1").listOrder == 5)
            assertTrue(fridgesDao.query("Test2").listOrder == 4)
            assertTrue(fridgesDao.query("Test3").listOrder == 98)

            assertTrue(bottlesDao.queryByFridge("Test1")[0] == bottle1)
        }
    }

    @Test
    fun updateFridgesTest() {
        val fridge1 = Fridge(1,"Test1", 5)
        val fridge2 = Fridge(2,"Test2", 4)
        val fridge3 = Fridge(3,"Test3", 98)

        runBlocking {
            fridgesDao.insert(fridge1, fridge2, fridge3)
        }

        fridge1.listOrder = 1
        fridge2.listOrder = 2
        fridge3.listOrder = 3

        fridge3.name = "Hehe"

        fridgesDao.update(fridge1, fridge2, fridge3)

        assertTrue(fridgesDao.query("Test1").listOrder == 1)
        assertTrue(fridgesDao.query("Test2").listOrder == 2)
        assertTrue(fridgesDao.query("Hehe").listOrder == 3)
    }

    @Test
    fun updateBottleTest() {
        val bottle1 = Bottle(1,"Test1", 5, 5, "Frid", 5, 28, 330)
        val bottle2 = Bottle(2,"Test3", 5, 5, "Frid", 6, 28, 330)
        val bottle3 = Bottle(3,"Test5", 5, 5, "Frid", 7, 28, 330)

        bottlesDao.insert(bottle1, bottle2, bottle3)

        bottle1.listOrder = 1
        bottle2.listOrder = 2
        bottle3.listOrder = 3

        bottle3.name = "Hehe"

        run {
            bottlesDao.update(bottle1, bottle2, bottle3)
        }


        assertTrue(bottlesDao.query(1)?.listOrder == 1)
        assertTrue(bottlesDao.query(2)?.listOrder == 2)
        assertTrue(bottlesDao.query(3)?.listOrder == 3)
    }
}