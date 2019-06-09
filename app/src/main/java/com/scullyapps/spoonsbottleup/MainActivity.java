package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mmenu_button_settings) Button btnSettings;
    @BindView(R.id.mmenu_button_exit)     Button btnExit;
    @BindView(R.id.mmenu_button_start)    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ActionBar toolbar = getSupportActionBar();
        toolbar.hide();

        setupButtons();
    }

    private void setupButtons() {
        btnSettings.setOnClickListener(v -> {
            Intent settings = new Intent();
            settings.setClass(this, SettingsActivity.class);
            startActivity(settings);
        });

        btnExit.setOnClickListener(v -> {
            System.exit(0);
        });
    }
}
