package com.example.closetics.clothes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.closetics.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClothesManager {

    public static void saveClothingRequest(Context context, ArrayList<MutableLiveData<String>> fragments,
    String URL, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String favorite = fragments.get(0).getValue();
        String size = fragments.get(1).getValue();
        String color = fragments.get(2).getValue();
        String dateBought = fragments.get(3).getValue();
        String brand = fragments.get(4).getValue();
        String itemName = fragments.get(5).getValue();
        String material = fragments.get(6).getValue();
        String price = fragments.get(7).getValue();


        //Create the json object of the saveClothing data
        JSONObject saveClothing = new JSONObject();



        //Use try catch blocks when creating JSON objects

        //Basic check will need to fix
        try {
            if (favorite.toLowerCase().trim() == "yes"){
                nullCheck("favorite", true, saveClothing);
            }else{
                nullCheck("favorite", false, saveClothing);
            }

            nullCheck("size", size, saveClothing);
            nullCheck("color", color, saveClothing);
            nullCheck("dateBought", dateBought, saveClothing);
            nullCheck("brand", brand, saveClothing);
            nullCheck("itemName", itemName, saveClothing);
            nullCheck("material", material, saveClothing);
            nullCheck("price", price, saveClothing);

        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                saveClothing, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private static void nullCheck(String header, Object parameter, JSONObject object) throws JSONException {
        if (parameter != null){
            object.put(header, parameter);
        }
    }



}
