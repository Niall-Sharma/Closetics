package com.example.closetics.outfits;

import android.content.Context;

import com.example.closetics.MainActivity;

import org.json.JSONObject;

public class EditOutfitClothingListItem {
    private long id;
    private long outfitId;
    private JSONObject jsonObject; // store it just in case
    private String name;
    private String color;
    private int typeId;
    private int specialTypeId;
    private Context context;

    public EditOutfitClothingListItem(Context context, JSONObject jsonObject, long id, long outfitId, String name, String color, int typeId, int specialTypeId) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.id = id;
        this.outfitId = outfitId;
        this.name = name;
        this.color = color;
        this.typeId = typeId;
        this.specialTypeId = specialTypeId;
    }

    public Context getContext() { return context; }

    public JSONObject getJsonObject() { return jsonObject; }

    public long getId() { return id; }

    public long getOutfitId() { return outfitId; }

    public String getName() { return name == null || name.isBlank() ? "(no name)" : name; }

    public String getColor() { return color == null || color.isBlank() ? "(no color)" : color; }

    public String getType() { return MainActivity.CLOTHING_TYPES.getOrDefault(typeId, "(no type)"); };

    public String getSpecialType() { return MainActivity.CLOTHING_SPECIAL_TYPES.getOrDefault(specialTypeId, "(no special type)"); };
}
