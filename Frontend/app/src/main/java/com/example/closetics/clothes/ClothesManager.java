package com.example.closetics.clothes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClothesManager {


    /*
    public static final Map<Integer, String> CLOTHING_TYPES = new HashMap<Integer, String>() {{
        put(1, "accessories");put(2, "activewear");put(3, "bottoms");put(4, "dresses");put(5, "footwear");put(6, "formalwear");put(7, "outerwear");put(8, "seasonal");put(9, "sleepwear");put(10, "tops");put(11, "undergarments");put(12, "workwear");
    }};
    public static final Map<Integer, String> CLOTHING_SPECIAL_TYPES = new HashMap<Integer, String>() {{
        put(81, "aprons");put(27, "a line dresses");put(13, "backpacks");put(10, "belts");put(51, "blazers");put(67, "blouses");put(28, "bodycon dresses");put(34, "boots");put(75, "boxers");put(73, "bras");put(76, "briefs");put(23, "capris");put(52, "cardigans");put(50, "coats");put(30, "cocktail dresses");put(6, "compression wear");put(72, "crop tops");put(45, "dress shirts");put(44, "evening gowns");put(38, "flats");put(36, "flip flops");put(48, "formal dresses");put(14, "glasses");put(11, "gloves");put(31, "gowns");put(5, "gym t shirts");put(12, "handbags");put(9, "hats");put(37, "heels");put(71, "hoodies");put(49, "jackets");put(17, "jeans");put(15, "jewelry");put(21, "leggings");put(39, "loafers");put(64, "loungewear");put(24, "maxi dresses");put(26, "midi dresses");put(25, "mini dresses");put(61, "nightgowns");put(7, "other");put(79, "overalls");put(40, "oxfords");put(60, "pajamas");put(77, "panties");put(56, "parkas");put(69, "polos");put(54, "ponchos");put(59, "rainwear");put(63, "robes");put(4, "running shorts");put(82, "safety gear");put(35, "sandals");put(8, "scarves");put(80, "scrubs");put(68, "shirts");put(19, "shorts");put(20, "skirts");put(62, "sleep shirts");put(41, "slippers");put(33, "sneakers");put(1, "sports bras");put(42, "suits");put(32, "sun dresses");put(22, "sweatpants");put(70, "sweatshirts");put(57, "swimwear");put(66, "tank tops");put(46, "ties");put(3, "tracksuits");put(18, "trousers");put(43, "tuxedos");put(65, "t shirts");put(74, "underwear");put(78, "uniforms");put(53, "vests");put(47, "waistcoats");put(16, "watches");put(55, "windbreakers");put(58, "winterwear");put(29, "wrap dresses");put(2, "yoga pants");
    }};

     */

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
