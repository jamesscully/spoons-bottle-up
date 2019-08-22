package com.scullyapps.spoonsbottleup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class DatabaseHelper extends SQLiteOpenHelper {

    protected String DB_NAME = "None";
    protected Context context;

    protected static SQLiteDatabase database;



    public DatabaseHelper(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        if(context.getDatabasePath(DB_NAME).exists())
            create();
    }



    /**
     * Copies the requested database from assets.
     */
    void create() {
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

            this.database = SQLiteDatabase.openDatabase(outFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);

        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
