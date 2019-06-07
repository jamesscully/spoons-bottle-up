package com.scullyapps.spoonsbottleup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottleDatabase db;
    int bottleCount;

    List<Plaque> plqs = new ArrayList<>();
    List<LinearLayout> containers = new ArrayList<>();
    List<Bottle> bottles = new ArrayList<>();

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

        containers.add((LinearLayout) findViewById(R.id.ctnrFirst));
        containers.add((LinearLayout) findViewById(R.id.ctnrIPA));
        containers.add((LinearLayout) findViewById(R.id.ctnrTonic));
        containers.add((LinearLayout) findViewById(R.id.ctnrBack));

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
        Button toggle = findViewById(R.id.btnHideShow);
        Button clr = findViewById(R.id.btnClear);


        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < bottleCount; i++) {
                    if(plqs.get(i).btlCount == 0)
                        plqs.get(i).toggle();
                }
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                for(Plaque p : plqs) {
                                    p.setCount(0);
                                    p.setVisibility(View.VISIBLE);
                                    p.hidden = false;
                                }
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);

                AlertDialog diag = builder.create();

                diag.show();


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
