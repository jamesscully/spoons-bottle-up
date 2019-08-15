package com.scullyapps.spoonsbottleup;

import android.content.Context;

import com.scullyapps.spoonsbottleup.ui.BottleDisplay;
import com.scullyapps.spoonsbottleup.ui.Plaque;

public class Bottle {

    private int id;
    private String name;
    private DrinkType type;
    private int step;
    private int max;
    private String fridgeName;
    private int listOrder;

    public static class Builder {

        private final static int DEFAULT_MAX = 10;

        private int id;
        private String name;
        private DrinkType type;
        private int step;
        private int max;
        private String fridgeName;
        private int listOrder;

        public Builder(int id) {
            this.id = id;
            this.name = "Unnamed";
            this.type = DrinkType.DUMMY;
            this.max = DEFAULT_MAX;
            this.fridgeName = "Default";
            this.listOrder = 0;
        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        public Builder type(DrinkType type) {
            this.type = type;

            return this;
        }

        public Builder max(int max) {
            this.max = max;

            return this;
        }

        public Builder step(int inc) {
            this.step = inc;

            return this;
        }

        public Builder fridge (String id) {
            this.fridgeName = id;

            return this;
        }

        public Builder order (int order) {
            this.listOrder = order;

            return this;
        }

        public Bottle build() {
            return new Bottle(this.id, this.name, this.type, this.max, this.step, this.fridgeName, this.listOrder);
        }
    }

    public Bottle(int id, String name, DrinkType type, int max, int step, String fridge, int order) {
        this.id   = id;
        this.name = name;
        this.type = type;
        this.max  = max;
        this.step = step;
        this.fridgeName = fridge;
        this.listOrder = order;

    }

    public int getStep() { return this.step; }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public DrinkType getType() {
        return this.type;
    }

    public int getMax() {
        return this.max;
    }

    public String getFridgeName() {
        return this.fridgeName;
    }

    public BottleDisplay toDisplay(Context context) {
        return new BottleDisplay(context, this);
    }

    public Plaque toPlaque(Context context) {
        return new Plaque(context, this);
    }


}


