package com.scullyapps.spoonsbottleup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class ExtBottleDatabase extends DatabaseHelper {

    private final static String      DATABASE_NAME = "ExtBottles.db";
    private final static String         TABLE_NAME = "ExtBottles";
    private final static String SQL_QUERY_ALLNAMES = "SELECT Name FROM " + TABLE_NAME;

    public ExtBottleDatabase(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
