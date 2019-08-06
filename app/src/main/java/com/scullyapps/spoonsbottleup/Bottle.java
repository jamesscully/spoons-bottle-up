package com.scullyapps.spoonsbottleup;

public class Bottle {

    private int id;
    private String name;
    private DrinkType type;
    private int step;
    private int max;

    public static class Builder {

        private final static int DEFAULT_MAX = 10;

        private int id;
        private String name;
        private DrinkType type;
        private int step;
        private int max;

        public Builder(int id) {
            this.id = id;
            this.name = "Unnamed";
            this.type = DrinkType.DUMMY;
            this.max = DEFAULT_MAX;
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

        public Bottle build() {
            Bottle create = new Bottle(this.id, this.name, this.type, this.max, this.step);

            return create;
        }
    }

    public Bottle(int id, String name, DrinkType type, int max, int step) {
        this.id   = id;
        this.name = name;
        this.type = type;
        this.max  = max;
        this.step = step;

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


}


