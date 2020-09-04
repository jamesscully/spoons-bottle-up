package com.scullyapps.spoonsbottleup.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.scullyapps.spoonsbottleup.models.Bottle;
import com.scullyapps.spoonsbottleup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottleDisplay extends ConstraintLayout {
    private Bottle bottle;
    private Context context;

    @BindView(R.id.bottle_display_text_fridge) TextView txtFridge;
    @BindView(R.id.bottle_display_text_name)   TextView txtName;
    @BindView(R.id.bottle_display_text_type)   TextView txtType;

    public BottleDisplay(Context context, Bottle bottle) {
        super(context);
        this.context = context;
        this.bottle = bottle;

        init();
    }

    public BottleDisplay(Context context, AttributeSet attrs, Bottle bottle) {
        super(context, attrs);
        this.context = context;
        this.bottle = bottle;
        init();
    }

    public BottleDisplay(Context context, AttributeSet attrs, int defStyleAttr, Bottle bottle) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.bottle = bottle;
        init();
    }

    private void init() {
        LayoutInflater.from(context).inflate(R.layout.fragment_bottle_display, this, true);
        ButterKnife.bind(this);

        txtName.setText(bottle.getName());
        txtType.setText("Max: " + bottle.getMax());
    }
}
