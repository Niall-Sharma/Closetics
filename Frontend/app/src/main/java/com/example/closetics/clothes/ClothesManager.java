package com.example.closetics.clothes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.closetics.MainActivity;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClothesManager {


    /*
    More work here adding more types and figuring out the best methods of input for them, currently just an edit text
     */

    public static void saveClothingRequest(Context context, ArrayList<MutableLiveData<String>> fragments, Long userId,
    String URL, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String favorite = fragments.get(0).getValue();
        String size = fragments.get(1).getValue();
        String color = fragments.get(2).getValue();
        String dateBought = fragments.get(3).getValue();
        String brand = fragments.get(4).getValue();
        String itemName = fragments.get(7).getValue();
        String material = fragments.get(6).getValue();
        String price = fragments.get(5).getValue();


        //Create the json object of the saveClothing data
        JSONObject saveClothing = new JSONObject();



        //Use try catch blocks when creating JSON objects

        //Basic check will need to fix
        try {
            if (favorite!=null) {
                if (favorite.toLowerCase().trim() == "yes") {
                    nullCheck("favorite", true, saveClothing);
                } else {
                    nullCheck("favorite", false, saveClothing);
                }
            }

            nullCheck("size", size, saveClothing);
            nullCheck("color", color, saveClothing);
            nullCheck("dateBought", dateBought, saveClothing);
            nullCheck("brand", brand, saveClothing);
            nullCheck("itemName", itemName, saveClothing);
            nullCheck("material", material, saveClothing);
            nullCheck("price", price, saveClothing);
            /*
            Note: Small naming error, user instead of userId for JSON object
             */
            saveClothing.put("user", userId);
            /*
            This is for testing only! Remember to remove!
             */
            saveClothing.put("clothingType", 1);

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

    public static void deleteClothingRequest(Context context, Long clothingId, String URL,
                                             Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {
        String deleteUrl = URL + "/" + clothingId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void updateClothingRequest(Context context, Long clothingId,
                                             Map<String, Object> updatedFields,
                                             String URL,
                                             Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {
        String updateUrl = URL + "/" + clothingId;
        JSONObject updateData = new JSONObject(updatedFields);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                updateData,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /*
    Note: This method is not for a JSON object but for an array
     */

    public static void getClothingByUserRequest(Context context, long userId, String URL,
                                                Response.Listener<JSONArray> responseListener,
                                                Response.ErrorListener errorListener) {
        String getUrl = URL + "/user/" + userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getUrl, null, responseListener, errorListener);



        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }




    private static void nullCheck(String header, Object parameter, JSONObject object) throws JSONException {
        if (parameter != null){
            object.put(header, parameter);
        }
    }



}
