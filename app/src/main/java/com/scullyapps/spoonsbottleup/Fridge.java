package com.scullyapps.spoonsbottleup;

import java.util.List;

public class Fridge {

          String name;
    List<Bottle> bottles;

    public Fridge(String name) {
        this.name = name;
    }

    public void addBottle(Bottle bottle) {
        bottles.add(bottle);
    }

    public void rmBottle(Bottle bottle) {
        for(Bottle b : bottles) {
            if(b.equals(bottle)) {
                bottles.remove(b);
            }
        }
    }

    public void serialize() {

    }

    public void deserialize() {

    }

}
