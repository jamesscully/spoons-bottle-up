package com.scullyapps.spoonsbottleup.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.DrinkType;
import com.scullyapps.spoonsbottleup.ui.BottleDisplay;

import java.util.ArrayList;
import java.util.List;

public class BottleDatabase extends DatabaseHelper {

    private final static String         TABLE_NAME = "Bottles";
    private final static String SQL_QUERY_ALLNAMES = "SELECT Name FROM " + TABLE_NAME;

    public BottleDatabase(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        this.DB_NAME = "Bottles.db";

        if(context.getDatabasePath(DB_NAME).exists()) {
            create();
        }
    }

    public List<BottleDisplay> getBottleDisplay() {
        List<Bottle> bottles = getBottles();

        List<BottleDisplay> ret = new ArrayList<>();

        for(Bottle b : bottles) {
            ret.add(new BottleDisplay(context, b));
        }

        return ret;
    }

    public List<Bottle> getBottles() {
        List<String> names = getNames();

        List<Bottle> bottles = new ArrayList<>();

        for(String s : names) {
            bottles.add(new Bottle(s, DrinkType.DUMMY));
        }

        return bottles;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        if(database == null) {
            names.add("SQL Error");
            return names;
        }

        Cursor cur = database.rawQuery(SQL_QUERY_ALLNAMES, null);

        cur.moveToFirst();

        while(!cur.isAfterLast()) {
            names.add(cur.getString(0));
            cur.moveToNext();
        }

        cur.close();

        return names;
    }
}
