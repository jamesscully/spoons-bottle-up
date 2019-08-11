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
import java.util.Random;

public class BottleDatabase extends DatabaseHelper {

    private final static String         TABLE_NAME = "Bottles";
    private final static String SQL_QUERY_ALLNAMES = "SELECT Name FROM " + TABLE_NAME;
    private final static String SQL_QUERY_ALLBOTTLES = "SELECT * FROM " + TABLE_NAME + " ORDER BY ListOrder DESC";

    public BottleDatabase(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        this.DB_NAME = "Bottles.db";

        create();

        //if(!context.getDatabasePath(DB_NAME).exists()) {
        //    create();
        //}
    }

    public List<BottleDisplay> getBottleDisplay() {
        List<Bottle> bottles = getBottles();

        List<BottleDisplay> ret = new ArrayList<>();

        for(Bottle b : bottles) {
            ret.add(new BottleDisplay(context, b));
        }

        return ret;
    }

    public List<String> getNames() {
        List<Bottle> bottles = getBottles();
        List<String> names = new ArrayList<>();

        for(Bottle b : bottles) {
            names.add(b.getName());
        }

        return names;
    }

    public List<Bottle> getBottles() {
        List<Bottle> bottles = new ArrayList<>();

        if(database == null) {
            bottles.add(new Bottle.Builder(0)
                            .name("SQLError")
                            .type(DrinkType.DUMMY)
                            .max(99).build());
            return bottles;
        }

        Cursor cur = database.rawQuery(SQL_QUERY_ALLBOTTLES, null);

        cur.moveToFirst();

        int prevFridgeNum = 0;

        while(!cur.isAfterLast()) {

                 int id = cur.getInt(0);
            String name = cur.getString(1);

            int tempFridgeNum = cur.getInt(2);

            if(tempFridgeNum != prevFridgeNum) {
                Random rand = new Random();
                bottles.add(new Bottle.Builder(rand.nextInt()).type(DrinkType.SPACER).name("SPACER").max(99).build());
            }

            // todo: implement these
            // DrinkType type = cur.getString(2);
            // int max = cur.getInt(3);

            bottles.add(new Bottle.Builder(id)
                                    .name(name)
                                    .type(DrinkType.DUMMY)
                                    .max(99).build());

            prevFridgeNum = tempFridgeNum;
            cur.moveToNext();
        }

        cur.close();

        return bottles;
    }
}
