package com.scullyapps.spoonsbottleup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

public class Plaque extends LinearLayout {

    private int color;
    private TextView pick;
    public int btlCount;
    public Bottle bottle;
    boolean hidden;

    Context thisContext;


    public Plaque(Context context, Bottle btl) {
        super(context); bottle = btl;
        LayoutInflater.from(context).inflate(R.layout.plaque_layout, this, true);
        thisContext = context;
        init(context, null, 0);
    }

    public Plaque(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.plaque_layout, this, true);
        init(context, attrs, 0);
    }

    public Plaque(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    public void setAccent() {
        color = 225;
        setBackgroundColor(Color.rgb(color, color, color));
    }

    public void setCount(int n) {
        btlCount = n;
        pick.setText(Integer.toString(btlCount));
    }


    public void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.Plaque, defStyleAttr, 0);
        try {
            setupPlaque();

            color = 230;
            setBackgroundColor(Color.rgb(color, color, color));

        } catch (Exception e) {
            Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            attr.recycle();
        }
    }

    public void setupPlaque() {
        TextView lblName = findViewById(R.id.strBtlName);
        TextView lblMax = findViewById(R.id.bottleMax);
        pick = findViewById(R.id.bottleMax);


        lblName.setText(bottle.name);
        lblMax.setText(Integer.toString(bottle.max));
        setupButtons();
        setupCounter();
    }

    public void setupButtons() {
        Button inc, dec, stepInc, stepDec;
        inc = findViewById(R.id.btnInc);
        dec = findViewById(R.id.btnDec);
        stepInc = findViewById(R.id.btnStepInc);
        stepDec = findViewById(R.id.btnStepDec);
        Button clr = findViewById(R.id.btnClear);



        inc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btlCount++;
                pick.setText(Integer.toString(btlCount));
            }
        });


        dec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btlCount - 1 >= 0) {
                    btlCount--;
                    pick.setText(Integer.toString(btlCount));
                }
            }
        });

        stepInc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true) {
                    btlCount += 5;
                    pick.setText(Integer.toString(btlCount));
                }
            }
        });

        stepDec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btlCount - 5 >= 0)
                    btlCount -= 5;
                else
                    setCount(0);

                pick.setText(Integer.toString(btlCount));
            }
        });


    }

    // code for setting up picker
    public void setupCounter() {
        pick.setText(Integer.toString(btlCount));
    }

    public void toggle() {
        if(!hidden)
            this.setVisibility(View.GONE);
        else
            this.setVisibility(View.VISIBLE);
        hidden = !hidden;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getResources().getDimensionPixelOffset(R.dimen.plaqueHeight));
    }

}
