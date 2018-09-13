package com.test.yames.bottleup;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottleDatabase db;
    int bottleCount;

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

        List<Bottle> bottles = db.getBottles(); // create bottle list + plaques
        List<LinearLayout> containers = new ArrayList<>();
        containers.add((LinearLayout) findViewById(R.id.ctnrFirst));
        containers.add((LinearLayout) findViewById(R.id.ctnrIPA));
        containers.add((LinearLayout) findViewById(R.id.ctnrTonic));
        containers.add((LinearLayout) findViewById(R.id.ctnrBack));

        Button toggle = new Button(this);
        toggle = findViewById(R.id.btnHideShow);



        final Plaque[] plqs = new Plaque[bottleCount];

        for(int i = 0; i < bottleCount; i++) {
            plqs[i] = new Plaque(getApplicationContext(), bottles.get(i));
            if(i % 2 == 1)
                plqs[i].setAccent();

            containers.get(bottles.get(i).fridge).addView(plqs[i]);
        }

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < bottleCount; i++) {
                    if(plqs[i].thisCount == 0)
                        plqs[i].toggle();
                }
            }
        });
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
