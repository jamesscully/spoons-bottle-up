package com.scullyapps.spoonsbottleup;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scullyapps.spoonsbottleup.ui.main.BottleListFragment;
import com.scullyapps.spoonsbottleup.ui.main.FridgeFragment;
import com.scullyapps.spoonsbottleup.ui.main.GeneralSettingsFragment;
import com.scullyapps.spoonsbottleup.ui.main.SectionsPagerAdapter;
import com.scullyapps.spoonsbottleup.ui.main.dummy.DummyContent;

public class SettingsActivity extends AppCompatActivity implements BottleListFragment.OnFragmentInteractionListener, FridgeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_pager, new GeneralSettingsFragment())
                .commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onListFragmentInteraction(Bottle item) {
        Toast.makeText(this, "Item Pressed" + item.getName(), Toast.LENGTH_SHORT).show();
    }
}