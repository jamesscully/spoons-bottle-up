package com.scullyapps.spoonsbottleup.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getIntOrNull
import com.scullyapps.spoonsbottleup.App
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

 object BottleDatabase {

    private const val TAG = "BottleDatabase"

    private lateinit var helper : SQLiteOpenHelper
    private lateinit var database : SQLiteDatabase

                  var DB_NAME = "Bottles.db"
    private const val BOTTLE_TABLE = "Bottles"
    private const val FRIDGE_TABLE = "Fridges"

    private const val SQL_QUERY_ALLBOTTLES = "SELECT * FROM $BOTTLE_TABLE ORDER BY ListOrder DESC"


    init {
        init()
    }

    fun getPath() : String {
        return App.getContext().getDatabasePath(DB_NAME).path
    }

    fun init() {
        val dbVersion = 1

        helper = object : SQLiteOpenHelper(App.getContext(), null, null, dbVersion) {
            override fun onCreate(p0: SQLiteDatabase?) { }
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { }
        }


        // read proper DB file from file from databases/
        if(!File(getPath()).exists())
            copyDatabaseFromAssets(App.getContext())

        database = SQLiteDatabase.openDatabase(getPath(), null, SQLiteDatabase.OPEN_READWRITE)
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
//            if(bottle.id.isNotEmpty()) {
//                Log.w(TAG, "Attempt to add bottle with non-empty ID, maybe update?")
//                return
//            }

            val values = ContentValues().apply {
                put(ID, bottle.id)
                put(NAME, bottle.name)
                put(LIST_ORDER, bottle.listOrder)
                put(STEP, bottle.step)
                put(MAX, bottle.max)
                put(FRIDGE, bottle.fridgeName)
            }

            database.insert(BOTTLE_TABLE, null, values)
        }


        fun get(id : String) : Bottle? {
            val cursor = database.rawQuery("SELECT * FROM $BOTTLE_TABLE WHERE ID='$id'", null)

            cursor.moveToFirst()

            if(cursor.count == 0) {
                Log.w(TAG, "BottleUtils.get(): Could not find bottle with ID $id ", )
                return null
            }

            return Bottle.fromCursor(cursor)
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

        const val NAME = "Name"
        const val LIST_ORDER = "ListOrder"

        fun getNames() : ArrayList<String> {
            val cursor = database.rawQuery("SELECT * FROM $FRIDGE_TABLE", null)
            val names = ArrayList<String>()

            cursor.forEachRow {cur ->
                names.add(cur.getString(0))
            }

            return names
        }

        fun getHighestListOrder() : Int? {
            val cursor = database.rawQuery("SELECT MAX(ListOrder) FROM $FRIDGE_TABLE", null)
            cursor.moveToFirst()

            return cursor.getIntOrNull(0)
        }

        fun add(name : String) {
            var listOrder = getHighestListOrder()

            Log.d(TAG, "Adding fridge $name")

            // return 0 if no fridges exist
            if(listOrder == null) {
                listOrder = 0
            } else {
                listOrder++
            }

            val cv = ContentValues()
                cv.put(NAME, name)
                cv.put(LIST_ORDER, listOrder)

            database.insert(FRIDGE_TABLE, null, cv)
        }

        fun get(name: String) : Fridge? {
            var listOrder : Int

            val cursor = database.rawQuery("SELECT * FROM $FRIDGE_TABLE WHERE $NAME = '$name'", null)

            if(cursor.count <= 0) {
                return null
            }

            cursor.moveToFirst()

            listOrder = cursor.getIntOrNull(1) ?: 0

            return Fridge(name, getBottles(name), listOrder)

        }

        fun getAll() : List<Fridge> {
            val cursor = database.rawQuery("SELECT * FROM $FRIDGE_TABLE", null)
            val fridges = ArrayList<Fridge>()

            cursor.forEachRow {cur ->
                val name = cur.getString(0)
                val listOrder = cur.getInt(1)
                fridges.add(Fridge(
                        name,
                        getBottles(name),
                        listOrder
                ))
            }

            return fridges
        }

        fun delete(name: String) {

            // no need for non-existent fridge
            val fridgeDelete = get(name) ?: return


            // update all bottles with blank/null names
            for(bottle in getBottles(name)) {
                bottle.fridgeName = ""
                BottleUtils.update(bottle.id, bottle)
            }


            // get fridges with a higher list order than us
            val toModify = getAll().filter {f ->
                f.listOrder > fridgeDelete.listOrder
            }

            // decrement order to ensure consistency
            toModify.forEach {fridge ->
                fridge.listOrder -= 1
                update(fridge)
            }

            database.delete(FRIDGE_TABLE, "Name = '$name'", null)
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

        fun getBottles(name: String = "", ascending: Boolean = true) : ArrayList<Bottle> {
            val bottles = ArrayList<Bottle>()

            val sortAs = if (ascending) "ASC" else "DESC"

            val SQL_NAME = "SELECT * FROM $BOTTLE_TABLE WHERE FridgeID='$name' ORDER BY ListOrder $sortAs"
            val SQL_NULL = "SELECT * FROM $BOTTLE_TABLE WHERE FridgeID IS NULL ORDER BY ListOrder $sortAs"

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


    val fridges: ArrayList<Fridge>
        get() {
            val fridges = ArrayList<Fridge>()
            val cursor = database.rawQuery("SELECT DISTINCT FridgeID FROM Bottles b JOIN Fridges f ON b.FridgeID = f.Name ORDER BY f.ListOrder", null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast && cursor.columnCount > 0) {
                val name = cursor.getString(0)
                val bottles = FridgeUtils.getBottles(name)
                val listOrder = FridgeUtils.get(name)?.listOrder ?: 0

                val newFridge = Fridge(
                        name,
                        bottles,
                        listOrder
                )

                fridges.add(0, newFridge)
                cursor.moveToNext()
            }

            return fridges
        }


    // This should only be used either when the db doesn't exist, or in a unit test
    fun copyDatabaseFromAssets(context: Context) {
        // where to write to (database/ path)
        val dbFile = App.getContext().getDatabasePath(DB_NAME)

        // from our assets folder
        val input  = App.getContext().assets.open("Bottles.db")
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