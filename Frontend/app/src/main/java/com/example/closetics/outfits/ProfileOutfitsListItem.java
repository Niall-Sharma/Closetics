package com.example.closetics.outfits;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.util.List;

public class ProfileOutfitsListItem {

    private Activity activity;
    private long id;
    private String name;
    private List<Long> clothingIds;
    private boolean isFavorite;

    public ProfileOutfitsListItem(Activity activity, long id, String name, List<Long> clothingIds, boolean isFavorite) {
        this.activity = activity;
        this.id = id;
        this.name = name;
        this.clothingIds = clothingIds;
        this.isFavorite = isFavorite;
    }

    public Activity getActivity() {
        return activity;
    }

    public Context getContext() {
        return activity.getApplicationContext();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getClothingIds() {
        return clothingIds;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
