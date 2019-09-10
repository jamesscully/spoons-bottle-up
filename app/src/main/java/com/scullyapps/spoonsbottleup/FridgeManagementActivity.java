package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.scullyapps.spoonsbottleup.database.BottleDatabase;
import com.scullyapps.spoonsbottleup.ui.Fridge;
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchCallback;
import com.scullyapps.spoonsbottleup.ui.fridgeman.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FridgeManagementActivity extends AppCompatActivity {

    @BindView(R.id.fridgeman_recycler)
    RecyclerView list;

    ArrayList<Bottle> bottles;

    List<Bottle> toRemove = new ArrayList<>();

    String fridgeName;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_management);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();

        Fridge fridge;


        BottleDatabase bottleDatabase = new BottleDatabase(this, null, null, 1);

        Toolbar toolbar = findViewById(R.id.toolbar_fridgeman);

        if(b != null) {
            fridgeName = b.getString("name", "NONE");

            fridge = new Fridge(this, fridgeName);

            fridge.setBottles(bottleDatabase.getBottlesByFridge(b.getString("name")));

            bottles = bottleDatabase.getBottlesByFridge(b.getString("name"));

            toolbar.setTitle("Editing " + fridgeName);
        }

        RecyclerListAdapter adapter = new RecyclerListAdapter(bottles);

        ItemTouchHelper.Callback callback = new ItemTouchCallback(adapter);

        itemTouchHelper = new ItemTouchHelper(callback);

        list = (RecyclerView) findViewById(R.id.fridgeman_recycler);

        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));

        itemTouchHelper.attachToRecyclerView(list);

        adapter.setIth(itemTouchHelper);




        for(int i = 0; i < bottles.size(); i++) {
            Bottle btl = bottles.get(i);
        }
    }

    @Override
    protected void onDestroy() {
        for(Bottle bottle : toRemove) {
            Log.d("DEBUG", "onDestroy: removing " + bottle.getName());
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        RecyclerListAdapter adapter = (RecyclerListAdapter) list.getAdapter();


        // if no changes made, then we can safely exit
        if(!adapter.modified) {
            super.onBackPressed();
            return;
        }


        AlertDialog confirm = new AlertDialog.Builder(this)
                .setMessage("Do you wish to save changes?")
                .setPositiveButton("Save changes", (dialog, which) -> {
                    List<Bottle> toChange = adapter.getItems();

                    if(toChange == null)
                        super.onBackPressed();

                    BottleDatabase bottleDatabase = new BottleDatabase(this, null, null, 1);


                    for(int i = 0; i < toChange.size(); i++) {

                        Bottle current = toChange.get(i);
                        bottleDatabase.updateListOrder(i, current.getId());
                        bottleDatabase.updateListOrder(99, 5);
                    }

                    super.onBackPressed();
                    dialog.cancel();
                })
                .setNegativeButton("Discard", (dialog, which) -> {
                    toRemove.clear();
                    super.onBackPressed();
                    dialog.cancel();
                })
                .create();

        confirm.show();
    }
}
