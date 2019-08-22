package com.scullyapps.spoonsbottleup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.scullyapps.spoonsbottleup.database.BottleDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FridgeManagementActivity extends AppCompatActivity {

    @BindView(R.id.fridgeman_layout_list)
    LinearLayout list;

    ArrayList<Bottle> bottles;

    List<Bottle> toRemove = new ArrayList<>();

    String fridgeName;

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

        for(int i = 0; i < bottles.size(); i++) {
            Bottle btl = bottles.get(i);

            BottleEntry check = new BottleEntry(this);
            check.setText(btl.getName());
            check.setChecked(true);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        toRemove.remove(btl);
                    else
                        toRemove.add(btl);
                }
            });

            list.addView(check);
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

        // if no changes made, then we can safely exit
        if(toRemove.isEmpty()) {
            super.onBackPressed();
            return;
        }

        BottleDatabase bottleDatabase = new BottleDatabase(this, null, null, 1);

        AlertDialog confirm = new AlertDialog.Builder(this)
                .setMessage("Do you wish to save changes?")
                .setPositiveButton("Save changes", (dialog, which) -> {
                    for(Bottle b : toRemove) {
                        bottleDatabase.removeFromFridge(b);
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

    private class BottleEntry extends AppCompatCheckBox {

        Context context;

        public BottleEntry(Context context) {
            super(context);
            this.context = context;

            setMinimumHeight(48);

        }
    }
}
