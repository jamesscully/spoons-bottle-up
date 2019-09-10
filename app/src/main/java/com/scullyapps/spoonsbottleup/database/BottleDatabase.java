package com.scullyapps.spoonsbottleup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.ui.Fridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BottleDatabase extends SQLiteOpenHelper {


    private final static String         TABLE_NAME = "Bottles";
    private final static String SQL_QUERY_ALLNAMES = "SELECT Name FROM " + TABLE_NAME;
    private final static String SQL_QUERY_ALLBOTTLES = "SELECT * FROM " + TABLE_NAME + " ORDER BY ListOrder DESC";
    private final static String SQL_QUERY_BYFRIDGE = "SELECT * FROM Bottles WHERE FridgeID = '";

    private Context context;
    private static SQLiteDatabase database;

    public BottleDatabase(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        String DB_NAME = "Bottles.db";

        if(database != null && database.isOpen()) {
            Log.d("BottleDatabase", "Database open, skipping");
            return;
        }

        try {

            InputStream input = context.getAssets().open(DB_NAME);

            File outFile = context.getDatabasePath(DB_NAME);

            OutputStream out = new FileOutputStream(outFile);

            byte[] buf = new byte[1024];
            int length;

            while( (length = input.read(buf)) > 0) {
                out.write(buf, 0, length);
            }

            out.flush();
            out.close();

            input.close();

            database = SQLiteDatabase.openDatabase(outFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

        } catch (IOException e ) {
            e.printStackTrace();
        }

    }


    public List<Bottle> getAllBottles() {

        List<Bottle> bottles = new ArrayList<>();

        if(database == null) {
            bottles.add(new Bottle.Builder().name("SQL Error occured").build());
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

    public void updateListOrder(int order, int id) {
        ContentValues cv = new ContentValues();
        cv.put("ListOrder", Integer.toString(order));

        database.update("Bottles", cv, "ID = " + id, null);

        Cursor test = database.rawQuery("SELECT ListOrder FROM Bottles WHERE ID=5", null);

        test.moveToFirst();

        Log.d("SQL RETURNED", test.getString(0));

    }

    public ArrayList<Bottle> getBottlesByFridge(@Nullable  String fridgeID) {

        ArrayList<Bottle> out = new ArrayList<>();

        String SQL;

        if(fridgeID == null)
            SQL = "SELECT * FROM Bottles WHERE FridgeID IS NULL";
        else
            SQL = SQL_QUERY_BYFRIDGE  + fridgeID + "' ORDER BY ListOrder";

        Cursor cur = database.rawQuery(SQL, null);

        Log.d("BottleDatabase ", "getBottlesByFridge: executing SQL " + SQL);

        cur.moveToFirst();

        while(!cur.isAfterLast() && cur.getCount() > 0) {
            out.add(getBottle(cur));
            cur.moveToNext();
        }

        cur.close();

        return out;
    }

    public ArrayList<Fridge> getFridges() {

        ArrayList<Fridge> out = new ArrayList<>();

        Cursor cur = database.rawQuery("SELECT DISTINCT FridgeID FROM Bottles b JOIN Fridges f ON b.FridgeID = f.Name ORDER BY f.ListOrder", null);

        cur.moveToFirst();

        while(!cur.isAfterLast() && cur.getColumnCount() > 0) {

            String name = cur.getString(0);

            if(name == null) {
                cur.moveToNext();
                continue;
            }

            Fridge add = new Fridge(context, name);

            add.setBottles(getBottlesByFridge(name));

            out.add(0, add);
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

        Log.d("DEBUG", "removeFromFridge: executing SQL: " + SQL);

        ContentValues cv = new ContentValues();
        cv.putNull("FridgeID");

        database.update("Bottles", cv, "Name='" + bottle.getName() + "'", null);
    }

    public Fridge getDefaultFridge(Context context) {
        Fridge ret = new Fridge(context, "Default");

        ret.setBottles(getBottlesByFridge(null));

        return ret;
    }

    private static Bottle getBottle(Cursor cursor) {
        Bottle.Builder builder = new Bottle.Builder();

        int id = Integer.parseInt(cursor.getString(0));

        return builder
                .id(id)
                .name(cursor.getString(1))
                .order(cursor.getInt(2))
                .step(cursor.getInt(3))
                .max(cursor.getInt(4))
                .fridge(cursor.getString(5))
                .build();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
