package com.example.closetics;

import java.util.List;

public class OutfitsListItem {
    private String name;
    private List<String> clothesNames;

    public OutfitsListItem(String name, List<String> clothesNames) {
        this.name = name;
        this.clothesNames = clothesNames;
    }

    public String getName() {
        return name;
    }

    public List<String> getClothesNames() {
        return clothesNames;
    }
}
