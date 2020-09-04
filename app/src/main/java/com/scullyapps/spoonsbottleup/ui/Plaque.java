package com.scullyapps.spoonsbottleup.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.scullyapps.spoonsbottleup.DrinkType;
import com.scullyapps.spoonsbottleup.models.Bottle;
import com.scullyapps.spoonsbottleup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Plaque extends LinearLayout {

    private Bottle bottle;
    private int count = 0;
    private Context context;

    public boolean inverted = false;

    @BindView(R.id.plaque_button_inc)     Button    btnInc;
    @BindView(R.id.plaque_button_dec)     Button    btnDec;
    @BindView(R.id.plaque_button_stepinc) Button    btnStepInc;
    @BindView(R.id.plaque_button_stepdec) Button    btnStepDec;
    @BindView(R.id.plaque_text_count)     TextView  txtCount;
    @BindView(R.id.plaque_text_name)      TextView  txtName;
    @BindView(R.id.plaque_text_max)       TextView  txtMax;

    int normalBgId = 0;

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
        txtMax.setText(String.format("/%3d", getMax()));

        setupButtons();

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                invert();
                return true;
            }
        });

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

    public void invert() {



        if(!inverted) {

            // use super class to temporarily modify color
            super.setBackgroundResource(R.color.colorPrimary);

            txtName.setTextColor(Color.WHITE);
            txtCount.setTextColor(Color.WHITE);

            txtMax.setVisibility(INVISIBLE);
        } else {

            // use our version to revert to accentized colour
            this.setBackgroundResource(normalBgId);

            txtName.setTextColor(Color.parseColor("#8A000000"));
            txtCount.setTextColor(Color.parseColor("#8A000000"));

            txtMax.setVisibility(VISIBLE);
        }

        inverted = !inverted;
    }

    public void invert(boolean b) {
        this.inverted = !b;
        invert();
    }

    public void setInputMode(boolean mode) {
        // enable buttons
        if(!mode) {
            btnStepDec.setVisibility(GONE);
            btnDec.setVisibility(GONE);

            btnStepInc.setVisibility(GONE);
            btnInc.setVisibility(GONE);
        } else {
            btnStepDec.setVisibility(VISIBLE);
            btnDec.setVisibility(VISIBLE);

            btnStepInc.setVisibility(VISIBLE);
            btnInc.setVisibility(VISIBLE);
        }
    }


    @Override
    public void setBackgroundResource(int resID) {
        super.setBackgroundResource(resID);
        this.normalBgId = resID;
    }

    public Bottle getBottle() { return bottle; }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        txtCount.setText(Integer.toString(count));
        this.count = count;
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


