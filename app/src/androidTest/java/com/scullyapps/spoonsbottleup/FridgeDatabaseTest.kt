package com.scullyapps.spoonsbottleup

import android.util.Log
import androidx.test.runner.AndroidJUnit4
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.data.BottleDatabase.DB_NAME
import com.scullyapps.spoonsbottleup.data.BottleDatabase.copyDatabaseFromAssets
import com.scullyapps.spoonsbottleup.data.BottleDatabase.init
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FridgeDatabaseTest {

    companion object {
        const val TAG = "DatabaseTests"
    }

    @Before
    fun setup() {
        DB_NAME = "TestDB.db"
        init()
        copyDatabaseFromAssets()
    }

    @Test
    fun testAddFridge() {
        Log.d(TAG, "testAddFridge: $DB_NAME")

        BottleDatabase.FridgeUtils.add("NewFridge")

        assertTrue(BottleDatabase.FridgeUtils.getNames().contains("NewFridge"))
    }

    @Test
    fun testDeleteFridge() {
        BottleDatabase.FridgeUtils.add("NewFridge")
        BottleDatabase.FridgeUtils.delete("NewFridge")

        Log.d(TAG, "testDeleteFridge: ${BottleDatabase.FridgeUtils.getNames().contains("NewFridge")}")

        assertFalse(BottleDatabase.FridgeUtils.getNames().contains("NewFridge"))
    }


    @Test
    fun testListOrderConsistency() {
        val fridges = BottleDatabase.FridgeUtils.getAll()

        // clear all fridges
        fridges.forEach {
            BottleDatabase.FridgeUtils.delete(it.name)
        }

        println("testListOrderConsistency: HighestOrder b4 add ${BottleDatabase.FridgeUtils.getHighestListOrder()}")

        BottleDatabase.FridgeUtils.add("Test1")
        BottleDatabase.FridgeUtils.add("Test2")
        BottleDatabase.FridgeUtils.add("Test3")

        println("testListOrderConsistency: HighestOrder after add ${BottleDatabase.FridgeUtils.getHighestListOrder()}")

        val test1 = BottleDatabase.FridgeUtils.get("Test1")
        var test2 = BottleDatabase.FridgeUtils.get("Test2")
        var test3 = BottleDatabase.FridgeUtils.get("Test3")

        assertEquals(0, test1?.listOrder)
        assertEquals(1, test2?.listOrder)
        assertEquals(2, test3?.listOrder)

        test2 = BottleDatabase.FridgeUtils.get("Test2")
        test3 = BottleDatabase.FridgeUtils.get("Test3")

        // Test0 should be 0 and Test3 should be 1
        BottleDatabase.FridgeUtils.delete("Test1")

        assertEquals(0, test1?.listOrder)
        assertEquals(1, test2?.listOrder)
    }

    @Test
    fun testUpdateFridge() {
        val fridge  = BottleDatabase.FridgeUtils.get("Cupboard")

        if(fridge == null) {
            fail()
            return
        }

        val bottles = fridge.bottles

        fridge.name = "TestFridge"

        BottleDatabase.FridgeUtils.update(fridge)

        for(b in bottles) {
            assertEquals(b.fridgeName, "TestFridge")
        }
    }


}