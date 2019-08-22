package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;

import com.scullyapps.spoonsbottleup.database.BottleDatabase;
import com.scullyapps.spoonsbottleup.ui.Plaque;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountActivity extends AppCompatActivity {


    @BindView(R.id.count_toolbar)         Toolbar toolbar;
    @BindView(R.id.count_layout_scroll)   ScrollView scrollView;
    @BindView(R.id.count_layout_main)     LinearLayout mainContainer;
    @BindView(R.id.count_button_bottleup) Button btnBottleup;

    boolean bottling_up = false;

    ArrayList<Fridge> fridges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        ButterKnife.bind(this);

        App app = new App();

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
            actionBar.hide();

        BottleDatabase db = new BottleDatabase(this, null, null, 1);

        // get all fridges
        fridges = db.getFridges();

        // add default fridge to front
        Fridge def = db.getDefaultFridge(this);

        if(def.getSize() > 0)
            fridges.add(def);

        for(Fridge f : fridges) {
            mainContainer.addView(f, 0);
        }

        Space padding = new Space(this);
        mainContainer.addView(padding);
        padding.setVisibility(View.INVISIBLE);

        btnBottleup.setOnClickListener(v -> {

            for(Fridge f : fridges)
                f.bottleUp(!bottling_up);

            // we're done here; invert flag
            bottling_up = !bottling_up;
        });

    }
}
