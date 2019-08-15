package com.scullyapps.spoonsbottleup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scullyapps.spoonsbottleup.ui.Plaque;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fridge extends LinearLayout {

          String  name;
    List<Integer> ids;
    Context       context;

    @BindView(R.id.txt_fridgename)
    TextView fridgeName;

    @BindView(R.id.layout_widget_fridge)
    LinearLayout fridgeLayout;


    public Fridge(Context context, String name) {
        super(context);
        this.name = name;
        this.context = context;

        init();
    }


    private void init() {
        LayoutInflater.from(context).inflate(R.layout.widget_fridge, this, true);

        ButterKnife.bind(this);

        fridgeName.setText(name);

        ids = new ArrayList<>();
    }

    public void addBottle(Bottle bottle) {
        ids.add(bottle.getId());

        fridgeLayout.addView(new Plaque(context, bottle));

        serialize();
    }

    public void rmBottle(Bottle bottle) {
        for(Integer id : ids) {
            if(id.equals(bottle)) {
                ids.remove(id);
            }
        }
    }

    public String serialize() {
        Gson serial = new GsonBuilder().setPrettyPrinting().create();
        Log.i(String.format("Writing Json for %s", name), serial.toJson(ids) + "\n");
        return serial.toJson(ids);
    }

    public static Fridge deserialize(String json) {

        return null;
    }

    public boolean hasBottle(int id) {
        return ids.contains(id);
    }

    public String getName() {
        return name;
    }

}
