package com.example.closetics.dashboard;

import android.graphics.Bitmap;
import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClothingStatItem {

    private JSONObject object;
    private byte[] image;
    //private ArrayList<Bitmap> bitmap;
    //private ArrayList<byte[]>
    private String name;
    private long clothesId;
    private boolean wornToday;
    private String numberOfOutfitsIn;
    private String wornCount;

    // Constructor without image and wornToday
    public ClothingStatItem(JSONObject object, String name, long clothesId) {
        this.object = object;
        this.name = name;
        this.clothesId = clothesId;
        //this.numberOfOutfitsIn = numberOfOutfitsIn;
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

    public void setNumberOfOutfitsIn(String numberOfOutfitsIn) {
        this.numberOfOutfitsIn = numberOfOutfitsIn;
    }
    public void setImage(byte[] image){
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
    public Long getOutfitId(){
        return clothesId;
    }

    public String getWornCount(){
        return wornCount;
    }

    public void setWornCount(String wornCount) {
        this.wornCount = wornCount;
    }

    public String getName(){
        return name;
    }
    public String getTimesWorn() throws JSONException {
        return object.getString("timesWorn");
    }
    public String getAvgHighTemp() throws JSONException{
        return object.getString("avgHighTemp");
    }
    public String getAvgLowTemp()throws JSONException{
        return object.getString("avgLowTemp");
    }
    public String getNumberOfOutfitsIn(){
        return numberOfOutfitsIn;
    }




}
