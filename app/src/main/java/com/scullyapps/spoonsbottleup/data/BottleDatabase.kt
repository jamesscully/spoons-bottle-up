package com.scullyapps.spoonsbottleup.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.scullyapps.spoonsbottleup.App
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.Fridge
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

object BottleDatabase {

    private var helper : SQLiteOpenHelper
    private lateinit var database : SQLiteDatabase
    private var context : Context

    private const val DB_NAME = "Bottles.db"
    private const val BOTTLE_TABLE = "Bottles"
    private const val FRIDGE_TABLE = "Fridges"

    private const val SQL_QUERY_ALLNAMES = "SELECT Name FROM $BOTTLE_TABLE"
    private const val SQL_QUERY_ALLBOTTLES = "SELECT * FROM $BOTTLE_TABLE ORDER BY ListOrder DESC"
    private const val SQL_QUERY_BYFRIDGE = "SELECT * FROM Bottles WHERE FridgeID = '"


    init {

        val dbVersion = 1

        context = App.getContext()

        helper = object : SQLiteOpenHelper(context, null, null, dbVersion) {
            override fun onCreate(p0: SQLiteDatabase?) { }
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { }
        }

        val dbPath = context.getDatabasePath(DB_NAME).path
        // read proper DB file from file from databases/

        if(!File(dbPath).exists())
            copyDatabaseFromAssets()

        database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE)

    }


    val bottles: List<Bottle>
        get() {
            val bottles: MutableList<Bottle> = ArrayList()
            val cursor = database.rawQuery(SQL_QUERY_ALLBOTTLES, null)

            cursor.moveToFirst()

            while (!cursor.isAfterLast) {
                bottles.add(Bottle.fromCursor(cursor))
                cursor.moveToNext()
            }

            cursor.close()
            return bottles
        }

    fun setBottleListOrder(order: Int, id: String) {
        val cv = ContentValues()
            cv.put("ListOrder", order.toString())

        val affected = database.update("Bottles", cv, "ID = $id", null)

        Log.d("BottlesDB", "$id Rows affected = $affected")
    }

    fun getBottlesByFridge(fridgeID: String?): ArrayList<Bottle> {
        val bottles = ArrayList<Bottle>()
        var SQL = ""

        // if this fridge doesn't exist, load unlisted bottles
        SQL = if (fridgeID == null)
            "SELECT * FROM Bottles WHERE FridgeID IS NULL"
        else
            "$SQL_QUERY_BYFRIDGE$fridgeID' ORDER BY ListOrder"

        val cursor = database.rawQuery(SQL, null)

        Log.d("BottleDatabase ", "getBottlesByFridge: executing SQL $SQL")

        cursor.moveToFirst()
        while (!cursor.isAfterLast && cursor.count > 0) {
            bottles.add(Bottle.fromCursor(cursor))
            cursor.moveToNext()
        }

        cursor.close()
        return bottles
    }

    fun SQLiteDatabase.forAll() {

    }

    val fridges: ArrayList<Fridge>
        get() {
            val fridges = ArrayList<Fridge>()

            val cursor = database.rawQuery("SELECT DISTINCT FridgeID FROM Bottles b JOIN Fridges f ON b.FridgeID = f.Name ORDER BY f.ListOrder", null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast && cursor.columnCount > 0) {
                val name = cursor.getString(0)

                if (name == null) {
                    cursor.moveToNext()
                    continue
                }

                val newFridge = Fridge(context, name)
                    newFridge.bottles = getBottlesByFridge(name)

                fridges.add(0, newFridge)

                cursor.moveToNext()
            }

            cursor.close()
            return fridges
        }

    private fun getFridge(cursor: Cursor): Fridge {
        var out: Fridge? = null
        out = Fridge(context, cursor.getString(6))
        out.bottles = getBottlesByFridge(cursor.getString(6))
        return out
    }

    fun removeFromFridge(bottle: Bottle) {
        val SQL = "UPDATE Bottles SET FridgeID='' WHERE Name='" + bottle.name + "'"
        Log.d("DEBUG", "removeFromFridge: executing SQL: $SQL")
        val cv = ContentValues()
        cv.putNull("FridgeID")
        database.update("Bottles", cv, "Name='" + bottle.name + "'", null)
    }

    fun getDefaultFridge(context: Context): Fridge {
        val ret = Fridge(context, "Default")
        ret.bottles = getBottlesByFridge(null)
        return ret
    }


    /* Fridge CRUD */

    fun createFridge(fridge : String) {
        val cv = ContentValues().apply {
            put(Fridge.SQL.NAME, fridge)
        }

        database.insert(FRIDGE_TABLE, "", cv)
    }

    fun updateFridge(fridge: Fridge) {
        for(bottle in fridge.bottles) {
            // update all bottles to match
            val cv = ContentValues()
            cv.put("FridgeID", fridge.name)
            database.update(BOTTLE_TABLE, cv, "id=${bottle.id}", null)
        }

    }

    fun deleteFridge(fridgeName : String) {
        val cv = ContentValues()
        cv.putNull("FridgeID")

        // remove the fridge from our list
        database.delete(FRIDGE_TABLE, "Name=$fridgeName", null)

        // remove any bottles association with this fridge
        database.update(BOTTLE_TABLE, cv, "FridgeID=$fridgeName", null)
    }

    private fun copyDatabaseFromAssets() {
        // where to write to (database/ path)
        val dbFile = context.getDatabasePath(DB_NAME)

        // from our assets folder
        val input  = context.assets.open(DB_NAME)
        val output: OutputStream = FileOutputStream(dbFile)

        // kb buffer
        val buffer = ByteArray(1024)

        var length: Int

        while (input.read(buffer).also { length = it } > 0) {
            output.write(buffer, 0, length)
        }

        // flush and clean up
        output.flush(); output.close(); input.close()
    }
}