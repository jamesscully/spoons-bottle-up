package com.scullyapps.spoonsbottleup.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.scullyapps.spoonsbottleup.Bottle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BottleDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "Bottle.db";
    public static final String DB_PATH = "/data/data/com.scullyapps.spoonsbottleup/";
    private SQLiteDatabase db;
    private Context thisContext;

    public BottleDatabase(Context context) {
        super(context, DB_NAME, null, 1);
        thisContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDB() {
        boolean exist = checkDB();

        if(!exist) {
            this.getReadableDatabase();
            try {
                copyDB();
            } catch (IOException e) {
                throw new Error("Couldn't copy DB!");
            }
        }
    }

    public Boolean checkDB() {
        SQLiteDatabase cDB = null;

        try {
            String tryPath = DB_PATH + DB_NAME;
        } catch (SQLiteException e) {

        }

        if(cDB != null) {
            cDB.close();
            return true;
        }
        return false;
    }

    private void copyDB() throws IOException {
        InputStream input = thisContext.getAssets().open(DB_NAME);
        String oFile = DB_PATH + DB_NAME;

        OutputStream out = new FileOutputStream(oFile);

        byte[] buf = new byte[1024];
        int length;
        while((length = input.read(buf)) > 0) {
            out.write(buf, 0, length);
        }

        out.flush();
        out.close();
        input.close();
    }

    public void openDB() {
        db = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(db != null)
            db.close();
        super.close();
    }

    public List<String> getNames() {

        List<String> names = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT BName FROM Bottle", null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            names.add(cur.getString(0));
            cur.moveToNext();
        }
        cur.close();
        return names;
    }

    public List<Integer> getMaxes() {
        List<Integer> maxes = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT BMax FROM Bottle", null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            maxes.add(cur.getInt(0));
            cur.moveToNext();
        }
        cur.close();
        return maxes;
    }

    public List<Bottle> getBottles() {
        List<Bottle> bottles = new ArrayList<>();

        Bottle temp; // temp Bottle object
        String name; // name
        int max, fridge; // max, fridge num

        Cursor cur = db.rawQuery("SELECT BName, BMax, BFridge FROM Bottle", null);
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            temp = new Bottle(
                    cur.getString(0),   // construct bottle
                    cur.getInt(1),
                    cur.getInt(2)
            );
            bottles.add(temp); // add to list
            cur.moveToNext();
        }
        cur.close();
        return bottles;
    }

    public long getCount() {
        long count = DatabaseUtils.queryNumEntries(db, "Bottle");
        return count;
    }

}
