package com.scullyapps.spoonsbottleup;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.scullyapps.spoonsbottleup.data.BottleDatabase;
import com.scullyapps.spoonsbottleup.widgets.Plaque;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottleDatabase db;
    int bottleCount;

    List<Plaque> plqs             = new ArrayList<>();
    List<LinearLayout> containers = new ArrayList<>();
    List<Bottle> bottles          = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        db = new BottleDatabase(this);  // create connection to db
        db.createDB();
        db.openDB();

        bottleCount = ((int) db.getCount());
        bottles = db.getBottles(); // create bottle list + plaques

        /*
        containers.add((LinearLayout) findViewById(R.id.ctnrFirst));
        containers.add((LinearLayout) findViewById(R.id.ctnrIPA));
        containers.add((LinearLayout) findViewById(R.id.ctnrTonic));
        containers.add((LinearLayout) findViewById(R.id.ctnrBack));
        */

        setupPlaque();
        setupButtons();


    }

    public void setupPlaque() {
        for(int i = 0; i < bottleCount; i++) {
            plqs.add(i, new Plaque(getApplicationContext(), bottles.get(i)));
            Plaque curPlq = plqs.get(i);
            if(i % 2 == 1)
                curPlq.setAccent();

            containers.get(bottles.get(i).fridge).addView(curPlq);
        }
    }

    public void setupButtons() {

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
