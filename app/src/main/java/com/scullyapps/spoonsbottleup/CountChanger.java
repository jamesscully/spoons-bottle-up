package com.scullyapps.spoonsbottleup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class CountChanger extends LinearLayout {

    private int nMin, nMax, nDef, nStep;   // min/max, default and step
    private Button btnMin = null, btnAdd = null;
    private NumberPicker pck = null;
    public int count = 0;

    public CountChanger(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("YEET", "### START ######################");
        init(context, attrs);
    }



    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CountChanger, 0, 0);
        try {
            /*
            nMin = arr.getInteger(R.styleable.CountChanger_numMin, 0);
            nMax = arr.getInteger(R.styleable.CountChanger_numMax, 9999);
            nDef = arr.getInteger(R.styleable.CountChanger_numDef, 1);
            nStep = arr.getInteger(R.styleable.CountChanger_numStep, 1);

            btnAdd = (Button) findViewById(R.id.btnInc);
            btnMin = (Button) findViewById(R.id.btlMinus);
            pck = (NumberPicker) findViewById(R.id.npCnt);

            Log.d("YEET", btnAdd.getText().toString());

            count = nDef;

            String[] hey = {"1", "2", "5"};

            pck.setMinValue(nMin);
            pck.setMaxValue(nMax);
            pck.setDisplayedValues(hey);
            pck.setValue(5); */

        } catch (Exception e) {
            Log.d("YEET", "error occured");
            e.printStackTrace();

        } finally {
            arr.recycle();
            Log.d("YEET", "### END ######################");
        }
    }


    private void incCount() {
        count++;
    }
}
