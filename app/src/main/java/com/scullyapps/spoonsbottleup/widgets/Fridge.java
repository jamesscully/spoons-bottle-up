package com.scullyapps.spoonsbottleup.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.R;
import com.scullyapps.spoonsbottleup.interfaces.FridgeInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fridge extends LinearLayout implements FridgeInterface {

    String name;
    Context context;

    ArrayList<Bottle> bottles;

    @BindView(R.id.fridgeBtlContainer)
    LinearLayout bottleContainer;

    @BindView(R.id.fridgeName)
    TextView fridgeName;

    public Fridge(Context context, String name) {
        super(context);
        this.context = context;
        this.name = name;
        init();
    }

    public Fridge(Context context, AttributeSet attrs, String name) {
        super(context, attrs);
        this.context = context;
        this.name = name;
        init();
    }

    public Fridge(Context context, AttributeSet attrs, int defStyleAttr, String name) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.name = name;
        init();
    }

    public void init() {
        LayoutInflater.from(context).inflate(R.layout.fridge_layout, this, true);
        ButterKnife.bind(this);

        fridgeName.setText(name);
        bottles = new ArrayList<>();
    }


    @Override
    public void addBottle(Bottle bottle) {

        if(bottle == null) {
            Log.e("Fridge addBottle", "Bottle was null!");
            return;
        }

        bottleContainer.addView(new Plaque(context, bottle));
        bottles.add(bottle);
    }

    @Override
    public void addBottles(List<Bottle> bottles) {

        if(bottles.isEmpty())
            return;

        for(Bottle b : bottles)
            addBottle(b);
    }

    @Override
    public boolean removeBottleByName(String name) {

        int index = 0;
        for(Bottle b : bottles) {
            if(b.name.equals(name)) {
                bottles.remove(index);
                bottleContainer.removeViewAt(index);
                return true;
            }
            index++;
        }

        return false;
    }
}
