package com.example.closetics.dashboard;

import android.media.Image;

import org.json.JSONObject;

public class ClothingStatItem {

    private JSONObject object;
    private Image image;
    private String name;
    private long clothesId;
    private boolean wornToday;
    private String numberOfOutfitsIn;

    // Constructor without image and wornToday
    public ClothingStatItem(JSONObject object, String name, long clothesId, String numberOfOutfitsIn) {
        this.object = object;
        this.name = name;
        this.clothesId = clothesId;
        this.numberOfOutfitsIn = numberOfOutfitsIn;
        this.wornToday = false; // default value
    }

    // Constructor without image but with wornToday
    public ClothingStatItem(JSONObject object, String name, long clothesId, boolean wornToday, String numberOfOutfitsIn) {
        this.object = object;
        this.name = name;
        this.clothesId = clothesId;
        this.wornToday = wornToday;
        this.numberOfOutfitsIn = numberOfOutfitsIn;
    }


}
