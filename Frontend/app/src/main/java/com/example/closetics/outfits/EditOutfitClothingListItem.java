package com.example.closetics.outfits;

import android.content.Context;

import com.example.closetics.MainActivity;

import org.json.JSONObject;

public class EditOutfitClothingListItem {
    private long id;
    private JSONObject jsonObject; // store it just in case
    private String color;
    private int typeId;
    private int specialTypeId;
    private Context context;

    public EditOutfitClothingListItem(Context context, JSONObject jsonObject, long id, String color, int typeId, int specialTypeId) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.id = id;
        this.color = color;
        this.typeId = typeId;
        this.specialTypeId = specialTypeId;
    }

    public Context getContext() { return context; }

    public JSONObject getJsonObject() { return jsonObject; }

    public long getId() { return id; }

    public String getColor() { return color; }

    public String getType() { return MainActivity.CLOTHING_TYPES.get(typeId); };

    public String getSpecialType() { return MainActivity.CLOTHING_SPECIAL_TYPES.get(specialTypeId); };
}
