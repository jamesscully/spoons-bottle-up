package com.scullyapps.spoonsbottleup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.DrinkType;
import com.scullyapps.spoonsbottleup.Fridge;
import com.scullyapps.spoonsbottleup.ui.BottleDisplay;

import java.util.ArrayList;
import java.util.List;

public class BottleDatabase extends DatabaseHelper {

    private final static String         TABLE_NAME = "Bottles";
    private final static String SQL_QUERY_ALLNAMES = "SELECT Name FROM " + TABLE_NAME;
    private final static String SQL_QUERY_ALLBOTTLES = "SELECT * FROM " + TABLE_NAME + " ORDER BY ListOrder DESC";
    private final static String SQL_QUERY_BYFRIDGE = "SELECT * FROM Bottles WHERE FridgeID = '";

    public BottleDatabase(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        this.DB_NAME = "Bottles.db";

        create();

    }

    public List<BottleDisplay> getBottleDisplay() {
        List<Bottle> bottles = getAllBottles();

        List<BottleDisplay> ret = new ArrayList<>();

        for(Bottle b : bottles) {
            ret.add(new BottleDisplay(context, b));
        }

        return ret;
    }

    public List<String> getNames() {
        List<Bottle> bottles = getAllBottles();
        List<String> names = new ArrayList<>();

        for(Bottle b : bottles) {
            names.add(b.getName());
        }

        return names;
    }

    public List<Bottle> getAllBottles() {

        List<Bottle> bottles = new ArrayList<>();

        if(database == null) {
            bottles.add(new Bottle.Builder(0).name("SQL Error occured").build());
            return bottles;
        }

        Cursor cur = database.rawQuery(SQL_QUERY_ALLBOTTLES, null);

        cur.moveToFirst();

        while(!cur.isAfterLast()) {
            bottles.add(getBottle(cur));
            cur.moveToNext();
        }

        cur.close();

        return bottles;
    }

    public ArrayList<Bottle> getBottlesByFridge(String fridgeID) {

        ArrayList<Bottle> out = new ArrayList<>();

        Cursor cur = database.rawQuery(SQL_QUERY_BYFRIDGE  + fridgeID + "' ORDER BY ListOrder", null);

        cur.moveToFirst();

        while(!cur.isAfterLast() && cur.getCount() > 0) {
            out.add(getBottle(cur));
            cur.moveToNext();
        }

        return out;
    }

    public ArrayList<Fridge> getFridges() {

        ArrayList<Fridge> out = new ArrayList<>();

        Cursor cur = database.rawQuery("SELECT DISTINCT FridgeID FROM Bottles", null);

        cur.moveToFirst();

        while(!cur.isAfterLast() && cur.getColumnCount() > 0) {

            String name = cur.getString(0);

            if(name.equals(""))
                name = "Default";

            Fridge add = new Fridge(context, name);



            out.add(add);
            cur.moveToNext();
        }

        cur.close();

        return out;
    }

    private Fridge getFridge(Cursor cursor) {

        Fridge out = null;

        out = new Fridge(context, cursor.getString(6));

        out.setBottles(getBottlesByFridge(cursor.getString(6)));

        return out;
    }

    public void removeFromFridge(Bottle bottle) {
        String SQL = "UPDATE Bottles SET FridgeID='' WHERE Name='" + bottle.getName() + "'";

        Log.d("DEBUG", "removeFromFridge: db is open: " + database.isOpen());
        Log.d("DEBUG", "removeFromFridge: executing SQL: " + SQL);

        ContentValues cv = new ContentValues();
        cv.putNull("FridgeID");

        database.update("Bottles", cv, "Name='" + bottle.getName() + "'", null);
    }

    private Bottle getBottle(Cursor cursor) {
        Bottle.Builder builder = new Bottle.Builder(cursor.getInt(0));

        return builder.name(cursor.getString(1))
                .order(cursor.getInt(2))
                .step(cursor.getInt(3))
                .max(cursor.getInt(4))
                .fridge(cursor.getString(5))
                .build();
    }
}
