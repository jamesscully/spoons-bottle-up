package com.scullyapps.spoonsbottleup.database;

import android.app.Application;
import android.app.Service;
import android.os.Environment;
import android.util.Log;

import com.scullyapps.spoonsbottleup.App;
import com.scullyapps.spoonsbottleup.Fridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public enum  FridgeRepository {
    INSTANCE;

    ArrayList<Fridge> loaded;

    public final static String DEFAULT_PATH = App.getContext().getExternalFilesDir(null).getAbsolutePath() + "fridges/";
    public final static File   DEFAULT_DIR = new File(DEFAULT_PATH);

    FridgeRepository() {
        File toCreate = new File(App.getContext().getExternalFilesDir(null) + "fridges/");
        toCreate.mkdir();
    }

    public void loadAll() {

        File directory = new File(DEFAULT_PATH);
        File[] children;

        boolean exists = directory.mkdir();

        children = directory.listFiles();

        if(children == null)
            Log.e("FridgeRepository:", "Could not find any children");

        for(File f : children) {
            // load file
        }
    }

    public static boolean saveFridge(Fridge fridge) {
        try {
            FileOutputStream out = new FileOutputStream(DEFAULT_PATH + fridge.getName().trim());

            out.write(fridge.serialize().getBytes());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Fridge loadFridge(String name) {
        try {
            FileInputStream in = new FileInputStream(DEFAULT_PATH + name.trim());
            int len = in.available();

            byte[] b = new byte[len];



        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
}
