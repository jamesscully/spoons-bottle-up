package com.scullyapps.spoonsbottleup.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.DrinkType;
import com.scullyapps.spoonsbottleup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Plaque extends LinearLayout {

    private Bottle bottle;
    private int count = 0;
    private Context context;

    @BindView(R.id.plaque_button_inc)     Button    btnInc;
    @BindView(R.id.plaque_button_dec)     Button    btnDec;
    @BindView(R.id.plaque_button_stepinc) Button    btnStepInc;
    @BindView(R.id.plaque_button_stepdec) Button    btnStepDec;
    @BindView(R.id.plaque_text_count)     TextView  txtCount;
    @BindView(R.id.plaque_text_name)      TextView  txtName;

    public Plaque(Context context, Bottle bottle) {
        super(context);
        this.bottle = bottle;
        this.context = context;
        init();
    }

    public Plaque(Context context, @Nullable AttributeSet attrs, Bottle bottle) {
        super(context, attrs);
        this.bottle = bottle;
        this.context = context;
        init();
    }

    public Plaque(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Bottle bottle) {
        super(context, attrs, defStyleAttr);
        this.bottle = bottle;
        this.context = context;
        init();
    }

    public void init() {
        LayoutInflater.from(context).inflate(R.layout.widget_plaque, this, true);
        ButterKnife.bind(this);

        txtName.setText(bottle.getName());

        setupButtons();

    }

    public void setupButtons() {
        btnInc.setOnClickListener(v -> increment(1));
        btnDec.setOnClickListener(v -> decrement(1));
        btnStepDec.setOnClickListener(v -> decrement(bottle.getStep()));
        btnStepInc.setOnClickListener(v -> increment(bottle.getStep()));
    }


    public void increment(int amt) {

        Log.w(" [ Plaque fridge: " + getName() + " ]", "Adding " + amt + " to current count: " + count);

        // if for some reason we're incrementing <= 0, just add 1
        if(amt <= 0)
            count++;
        else if( (count + amt) > getMax())
            count = getMax();
        else
            count += amt;

        txtCount.setText(Integer.toString(count));
    }

    public void decrement(int amt) {

        Log.w(" [ Plaque fridge: " + getName() + " ]", "Removing " + amt + " from current count: " + count);

        if(count - amt < 0)
            count = 0;
        else
            count -= amt;

        txtCount.setText(Integer.toString(count));

    }


    public Bottle getBottle() { return bottle; }

    public int getCount() {
        return count;
    }

    public String getName() {
        return bottle.getName();
    }

    public int getMax() {
        return bottle.getMax();
    }

    public DrinkType getType() {
        return bottle.getType();
    }
}
