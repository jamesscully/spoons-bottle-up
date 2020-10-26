package com.scullyapps.spoonsbottleup

import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.data.BottleDatabase.DB_NAME
import com.scullyapps.spoonsbottleup.data.BottleDatabase.init
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    companion object {
        const val TAG = "UnitTest"
    }

    @Before
    fun setup() {
        DB_NAME = "TestDB.db"
        init()
    }

    @Test
    fun testAddFridge() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()


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
}