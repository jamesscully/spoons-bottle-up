package com.scullyapps.spoonsbottleup.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.R;
import com.scullyapps.spoonsbottleup.ui.Plaque;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fridge extends LinearLayout {

          String  name;
    ArrayList<Bottle> bottles;
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

        bottles = new ArrayList<>();
    }

    public void addBottle(Bottle bottle) {
        bottles.add(bottle);
        fridgeLayout.addView(new Plaque(context, bottle));
    }

    public void setBottles(ArrayList<Bottle> bottles) {
        for(Bottle b : bottles)
            addBottle(b);

        accentize();
    }

    public void rmBottle(Bottle bottle) {
        for(Bottle b : bottles) {
            if(b.equals(bottle)) {
                bottles.remove(b);
            }
        }
    }


    public boolean hasBottle(int id) {
        return bottles.contains(id);
    }

    public String getName() {
        return name;
    }

    public void bottleUp(boolean hide) {

        // used to determine if we've hid anything; can hide the entire thing else
        boolean modified = false;

        ArrayList<Plaque> plaques = getPlaques();


        // if we're not in bottling up, then show everything
        if(!hide) {
            this.setVisibility(VISIBLE);

            for(Plaque p : plaques) {
                p.setVisibility(VISIBLE);
                p.setInputMode(true);
            }

            return;
        }

        // if we are, hide only those with amt = 0
        for(Plaque p : plaques) {
            int amt = p.getCount();



            if(amt == 0) {
                p.setVisibility(GONE);
            } else {
                // if we're here, then we've had a plaque with count > 0

                if(p.inverted) {
                    p.setCount(p.getMax() - p.getCount());
                    p.invert(false);
                }

                p.setInputMode(false);

                modified = true;
            }
        }

        if(!modified) {
            // hide all this; hides fridge headers if not needed
            this.setVisibility(GONE);
        }

        accentize();
    }

    public void accentize() {
        boolean accent = false;

        for(Plaque p : getPlaques()) {
            if(p.getVisibility() == VISIBLE) {
                if(accent)
                    p.setBackgroundResource(R.color.plaqueBackgroundAcc);
                else
                    p.setBackgroundResource(R.color.plaqueBackground);

                accent = !accent;
            }
        }
    }

    public ArrayList<Plaque> getPlaques() {

        ArrayList<Plaque> ret = new ArrayList<>();

        for(int i = 0; i < fridgeLayout.getChildCount(); i++) {
            if(fridgeLayout.getChildAt(i) instanceof Plaque) {
                ret.add( (Plaque) fridgeLayout.getChildAt(i) );
            }
        }

        return ret;
    }


    public int getSize() {
        return bottles.size();
    }




}
