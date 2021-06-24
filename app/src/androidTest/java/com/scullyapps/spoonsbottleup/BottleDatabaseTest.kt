package com.scullyapps.spoonsbottleup

import android.util.Log
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.data.BottleDatabase.DB_NAME
import com.scullyapps.spoonsbottleup.data.BottleDatabase.copyDatabaseFromAssets
import com.scullyapps.spoonsbottleup.data.BottleDatabase.init
import com.scullyapps.spoonsbottleup.models.Bottle
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class BottleDatabaseTest {
    private val TAG: String = "BottleDatabaseTest"

    private var bottleA = Bottle("1234", "BottleA")
    private var bottleB = Bottle("12341234", "BottleB")


    @Before
    fun setup() {
        DB_NAME = "TestDB.db"
        init()
        copyDatabaseFromAssets(InstrumentationRegistry.getInstrumentation().targetContext)

        bottleA = Bottle("1234", "BottleA")
        bottleB = Bottle("12341234", "BottleB")

        BottleDatabase.BottleUtils.add(bottleA)
        BottleDatabase.BottleUtils.add(bottleB)
    }

    @Test
    fun testBottleAdd() {
        assertNotNull(BottleDatabase.BottleUtils.get("1234"))
        assertNotNull(BottleDatabase.BottleUtils.get("12341234"))
    }

    @Test
    fun testBottleDelete() {
        BottleDatabase.BottleUtils.delete(bottleA.id)
        Log.d(TAG, "testBottleDelete: ${BottleDatabase.BottleUtils.get(bottleA.id)}")
        assertNull(BottleDatabase.BottleUtils.get(bottleA.id))
    }

    @Test
    fun testBottleUpdate() {

    }

    @Test
    fun testBottleListOrderChange() {

    }

    @Test
    fun testBottleFridgeChange() {

    }
}