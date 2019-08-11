package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Toast;

import com.scullyapps.spoonsbottleup.database.BottleDatabase;
import com.scullyapps.spoonsbottleup.database.DatabaseHelper;
import com.scullyapps.spoonsbottleup.ui.Plaque;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountActivity extends AppCompatActivity {


    @BindView(R.id.count_toolbar)         Toolbar toolbar;
    @BindView(R.id.count_layout_scroll)   ScrollView scrollView;
    @BindView(R.id.count_layout_main)     LinearLayout mainContainer;
    @BindView(R.id.count_button_bottleup) Button btnBottleup;

    boolean bottling_up = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
            actionBar.hide();

        BottleDatabase db = new BottleDatabase(this, null, null, 1);

        List<Bottle> bottles = db.getBottles();

        for(int i = 0; i < bottles.size(); i++ ) {

            Plaque toAdd = new Plaque(this, bottles.get(i));

            mainContainer.addView(toAdd, 0);

            if(bottles.get(i).getType() == DrinkType.SPACER) {
                mainContainer.getChildAt(0).setVisibility(View.INVISIBLE);
            }

        }

        accentize();

        Space padding = new Space(this);
        mainContainer.addView(padding);
        padding.setVisibility(View.INVISIBLE);

        btnBottleup.setOnClickListener(v -> {
            for(int i = 0; i < mainContainer.getChildCount(); i++) {
                if(bottling_up) {
                    if(mainContainer.getChildAt(i) instanceof Plaque) {
                        Plaque exam = (Plaque) mainContainer.getChildAt(i);
                        Bottle btl = exam.getBottle();

                        if(btl.getType() != DrinkType.SPACER)
                            mainContainer.getChildAt(i).setVisibility(View.VISIBLE);

                    }
                }

                if(mainContainer.getChildAt(i) instanceof Plaque && !bottling_up) {
                    Plaque current = (Plaque) mainContainer.getChildAt(i);

                    if(current.getCount() == 0 && current.getBottle().getType() != DrinkType.SPACER) {
                        current.setVisibility(View.GONE);
                    }
                }
            }

            accentize();

            // we're done here; invert flag
            bottling_up = !bottling_up;
        });

    }

    private void accentize() {

        boolean accent = false;

        for(int i = 0; i < mainContainer.getChildCount(); i++) {

            Object o = mainContainer.getChildAt(i);

            if(!(o instanceof Plaque))
                continue;

            Plaque current = (Plaque) o;

            if(current.getVisibility() != View.VISIBLE)
                continue;

            if(accent)
                current.setBackgroundResource(R.color.plaqueBackgroundAcc);
            else
                current.setBackgroundResource(R.color.plaqueBackground);

            accent = !accent;
        }
    }

}
