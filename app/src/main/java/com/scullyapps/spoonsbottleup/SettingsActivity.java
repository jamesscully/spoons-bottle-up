package com.scullyapps.spoonsbottleup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Toast;

import com.scullyapps.spoonsbottleup.ui.Fridge;
import com.scullyapps.spoonsbottleup.ui.settings.BottleListFragment;
import com.scullyapps.spoonsbottleup.ui.settings.FridgeFragment;
import com.scullyapps.spoonsbottleup.ui.settings.GeneralSettingsFragment;
import com.scullyapps.spoonsbottleup.ui.settings.SectionsPagerAdapter;

public class SettingsActivity extends AppCompatActivity implements BottleListFragment.OnFragmentInteractionListener, FridgeFragment.OnListFragmentInteractionListener {

    private int CURRENT_TAB = 0;

    private Toolbar toolbar;
    private Context context;
    private TabLayout  tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        toolbar = findViewById(R.id.toolbar_settings);
        context = this;

        setupListeners();

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
        Toast.makeText(this, "Item Pressed " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(Fridge item) {
        Intent i = new Intent(this, FridgeManagementActivity.class);
        i.putExtra("name", item.getName());

        startActivity(i);

    }

    private void setupListeners() {
        // menu options
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_add_bottle:
                        Toast.makeText(context, "Pressed adding fridge", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.action_add_fridge:
                        Toast.makeText(context, "Pressed adding fridge", Toast.LENGTH_LONG).show();
                        break;
                }

                return false;
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // update class-wide tab selection
                CURRENT_TAB = tab.getPosition();

                // 0 - general
                // 1 - bottles
                // 2 - fridges

                switch (CURRENT_TAB) {
                    case 0:

                        break;

                    case 1:
                        toolbar.inflateMenu(R.menu.menu_settings_bottles);
                        break;

                    case 2:
                        toolbar.inflateMenu(R.menu.menu_settings_fridges);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // remove previously inflated menus
                toolbar.getMenu().clear();
            }

            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}