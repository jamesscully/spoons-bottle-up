package com.scullyapps.spoonsbottleup.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.scullyapps.spoonsbottleup.App
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import com.scullyapps.spoonsbottleup.ui.FridgeView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object BottleDatabase {

    private const val TAG = "BottleDatabase"

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

    // Simple extension function to run code for each row
    private fun Cursor.forEachRow(callback : (Cursor) -> Unit) {

        // no rows = do nothing!
        if(count <= 0 || columnCount <= 0) {
            return
        }

        moveToFirst()
        while(!isAfterLast) {
            callback(this)
            moveToNext()
        }
        close()
    }

    object BottleUtils {

        const val ID = "ID"
        const val NAME = "Name"
        const val LIST_ORDER = "ListOrder"
        const val STEP = "StepAmount"
        const val MAX = "MaxAmount"
        const val FRIDGE = "FridgeID"

        fun add(bottle: Bottle) {
            // default/new Bottle state should have empty ID
            if(bottle.id.isNotEmpty()) {
                Log.w(TAG, "Attempt to add bottle with non-empty ID, maybe update?")
                return
            }

            val values = ContentValues().apply {
//                put(ID, bottle.id)
                put(NAME, bottle.name)
                put(LIST_ORDER, bottle.listOrder)
                put(STEP, bottle.step)
                put(MAX, bottle.max)
                put(FRIDGE, bottle.fridgeName)
            }

            database.insert(BOTTLE_TABLE, null, values)
        }


        fun get(id : String) {
            database.rawQuery("SELECT * FROM $BOTTLE_TABLE WHERE ID=$id", null)
        }
        

        fun delete(id : String) {
            database.delete(BOTTLE_TABLE, "$ID=$id", null)
        }

        fun update(id : String, bottle: Bottle) {
            val cv = ContentValues().apply {
                put(LIST_ORDER, bottle.listOrder)
                put(NAME, bottle.name)
                put(STEP, bottle.step)
                put(MAX, bottle.max)
                put(FRIDGE, bottle.fridgeName)
            }

            database.update(BOTTLE_TABLE, cv, "$ID=$id", null)
        }
        
        fun setListOrder(id : String, order : Int) {
            val cv = ContentValues()
            cv.put("ListOrder", order.toString())

            val affected = database.update(BOTTLE_TABLE, cv, "ID = $id", null)

            Log.d("BottlesDB", "$id Rows affected = $affected")
        }
    }

    object FridgeUtils {

        val NAME = "Name"
        val LIST_ORDER = "ListOrder"

        fun add(name : String) {
            val cv = ContentValues()
                cv.put(NAME, name)

            database.insert(FRIDGE_TABLE, null, cv)
        }

        fun get(name: String) : Fridge? {
            var listOrder = 0

            val cursor = database.rawQuery("SELECT * FROM $FRIDGE_TABLE WHERE $NAME = $name", null)

            if(cursor.count <= 0) {
                return null
            }

            listOrder = cursor.getInt(1)

            return Fridge(name, getBottles(name), listOrder)

        }

        fun delete(name: String) {
            val values = ContentValues().apply {
                put(NAME, name)
            }

            for(bottle in getBottles(name)) {
                bottle.fridgeName = ""
                BottleUtils.update(bottle.id, bottle)
            }
        }

        fun update(fridge : Fridge) {
            val values = ContentValues().apply {
                put(LIST_ORDER, fridge.listOrder)
            }

            for(bottle in fridge.bottles) {
                bottle.fridgeName = fridge.name
                BottleUtils.update(bottle.id, bottle)
            }
        }

        fun getDefault() : Fridge {
            return Fridge("", emptyList()).apply {
                this.bottles = getBottles()
            }
        }

        fun getBottles(name: String = "") : ArrayList<Bottle> {
            val bottles = ArrayList<Bottle>()

            val SQL_NAME = "SELECT * FROM $BOTTLE_TABLE WHERE FridgeID='$name'"
            val SQL_NULL = "SELECT * FROM $BOTTLE_TABLE WHERE FridgeID IS NULL"

            val cursor: Cursor

            cursor = if(name.isEmpty()) {
                database.rawQuery(SQL_NULL, null)
            } else {
                database.rawQuery(SQL_NAME, null)
            }

            cursor.forEachRow {cur ->
                bottles.add(Bottle.fromCursor(cur))
            }

            return bottles
        }
    }


    val bottles: List<Bottle>
        get() {
            val bottles: MutableList<Bottle> = ArrayList()
            val cursor = database.rawQuery(SQL_QUERY_ALLBOTTLES, null)

            cursor.forEachRow {cur ->
                bottles.add(Bottle.fromCursor(cur))
            }

            return bottles
        }


    val fridges: ArrayList<FridgeView>
        get() {
            val fridges = ArrayList<FridgeView>()
            val cursor = database.rawQuery("SELECT DISTINCT FridgeID FROM Bottles b JOIN Fridges f ON b.FridgeID = f.Name ORDER BY f.ListOrder", null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast && cursor.columnCount > 0) {
                val name = cursor.getString(0)

                Log.d(TAG, "$name")

                val newFridge = FridgeView(context, name)
                newFridge.bottles = FridgeUtils.getBottles(name)

                fridges.add(0, newFridge)
                cursor.moveToNext()
            }

            return fridges
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