package com.scullyapps.spoonsbottleup;

public class Bottle {


    private String name;
    private int id;
    private DrinkType type;
    private int max;

    // default step for inc/dec is 5
    public int step = 5;

    public Bottle(String name, DrinkType type) {
        this.name = name;
        this.type = type;
    }

    public Bottle(String name, DrinkType type, int max) {
        this.name = name;
        this.type = type;
        this.max = max;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DrinkType getType() {
        return type;
    }

    public void setType(DrinkType type) {
        this.type = type;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
