package com.scullyapps.spoonsbottleup.interfaces;


import com.scullyapps.spoonsbottleup.Bottle;

import java.util.List;

public interface FridgeInterface {

    void addBottle(Bottle bottle);
    void addBottles(List<Bottle> bottles);

    void hideUnusedBottles();
    void unhideUnusedBottles();

    boolean removeBottleByName(String name);


}