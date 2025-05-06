package com.example.closetics.outfits;

import com.example.closetics.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitsListItem {
    private JSONObject jsonObject; // store it just in case
    private long id;
    private String name;
//    private JSONArray clothesIds;
    private List<JSONObject> clothes;
    private boolean isFavorite;

    public OutfitsListItem(JSONObject jsonObject, long id, String name, boolean isFavorite) {
        this.jsonObject = jsonObject;
        this.id = id;
        this.name = name;
        this.clothes = null;
        this.isFavorite = isFavorite;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<JSONObject> getClothes() {
        return clothes;
    }

    public void setClothes(List<JSONObject> clothes) {
        this.clothes = clothes;
    }

    public String getClothingName(int ind) {
        if (clothes == null || clothes.get(ind) == null) {
            return "null";
        }
        try {
            JSONObject clothing = clothes.get(ind);
            String name = "";
            if (clothing.has("itemName") && !clothing.getString("itemName").isEmpty()) {
                name += clothing.getString("itemName") + ", ";
            }
            name += clothing.getString("color") + " ";
            name += clothing.getString("brand") + " ";
            name += MainActivity.CLOTHING_SPECIAL_TYPES.get(clothing.getInt("specialType"));
            return name;
        } catch (JSONException e) {
            return "(no name)";
        }
    }

    public long getClothingId(int ind) {
        if (clothes == null || clothes.get(ind) == null) {
            return -1;
        }
        try {
            return clothes.get(ind).getLong("clothesId");
        } catch (JSONException e) {
            return -1;
        }
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
