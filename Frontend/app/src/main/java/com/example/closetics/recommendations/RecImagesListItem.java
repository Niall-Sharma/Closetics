package com.example.closetics.recommendations;

import android.content.Context;

public class RecImagesListItem {
    private long clothingId;
    private Context context;

    public RecImagesListItem(Context context, long clothingId) {
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
