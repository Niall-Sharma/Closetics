package com.example.closetics.outfits;

import android.content.Context;

public class OutfitClothesListItem {
    private long clothingId;
    private Context context;

    public OutfitClothesListItem(Context context, long clothingId) {
        this.context = context;
        this.clothingId = clothingId;
    }

    public Context getContext() {
        return context;
    }

    public long getClothingId() {
        return clothingId;
    }

    public void setClothingId(long clothingId) {
        this.clothingId = clothingId;
    }
}
