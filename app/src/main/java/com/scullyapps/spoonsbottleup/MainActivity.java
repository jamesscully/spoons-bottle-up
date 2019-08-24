package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scullyapps.spoonsbottleup.database.BottleDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mmenu_button_settings) Button btnSettings;
    @BindView(R.id.mmenu_button_exit)     Button btnExit;
    @BindView(R.id.mmenu_button_start)    Button btnStart;

    @BindView(R.id.mmenu_about_button)
    LinearLayout btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBar toolbar = getSupportActionBar();
        toolbar.hide();


        //        db.updateListOrder(0,0);

        setupButtons();
    }

    private void setupButtons() {

        Intent start = new Intent();

        btnStart.setOnClickListener(v -> {
            start.setClass(this, CountActivity.class);
            startActivity(start);
        });

        btnSettings.setOnClickListener(v -> {
            start.setClass(this, SettingsActivity.class);
            startActivity(start);
        });

        btnAbout.setOnClickListener(v -> {
            Toast.makeText(this, "Implement me!", Toast.LENGTH_SHORT).show();
        });

        btnExit.setOnClickListener(v -> {
            System.exit(0);
        });


    }
}
