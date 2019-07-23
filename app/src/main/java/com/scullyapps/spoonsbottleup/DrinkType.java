package com.scullyapps.spoonsbottleup;

public enum DrinkType {
    WINE("Wine"), CIDER("Cider"), BEER("Beer/Lager"), ALE("Ale"), SOFT("Soft Drink"), DUMMY("DUMMYNAME");

    private String name;

    DrinkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
