package com.example.closetics.outfits;

import static com.example.closetics.UserManager.SHARED_PREFERENCES_FILE_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.closetics.MainActivity;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Helper class with Outfit-related HTTP requests.
 */
public class OutfitManager {

    private static final String URL_GET_ALL_USER_OUTFITS = MainActivity.SERVER_URL + "/getAllUserOutfits/"; // + {{userId}}
    private static final String URL_GET_ALL_OUTFIT_CLOTHES = MainActivity.SERVER_URL + "/getAllOutfitItems/"; // + {{outfitId}}
    private static final String URL_GET_OUTFIT = MainActivity.SERVER_URL + "/getOutfit/"; // + {{outfitId}}
    private static final String URL_DELETE_OUTFIT = MainActivity.SERVER_URL + "/deleteOutfit/"; // + {{outfitId}}
    private static final String URL_PUT_UPDATE_OUTFIT = MainActivity.SERVER_URL + "/updateOutfit";
    private static final String URL_POST_CREATE_OUTFIT = MainActivity.SERVER_URL + "/createOutfit";
    private static final String URL_GET_ALL_USER_CLOTHES = MainActivity.SERVER_URL + "/getClothing/user/"; // + {{userId}}
    private static final String URL_PUT_REMOVE_ITEM = MainActivity.SERVER_URL + "/removeItemFromOutfit/"; // + {{outfitId}} + / + {{clothingId}}
    private static final String URL_PUT_ADD_ITEM = MainActivity.SERVER_URL + "/addItemToOutfit/"; // + {{outfitId}} + / + {{clothingId}}
    private static final String URL_PUT_REMOVE_LIKE = MainActivity.SERVER_URL + "/removeLike/"; // + {{outfitId}} + / + {{userId}}
    private static final String URL_PUT_ADD_LIKE = MainActivity.SERVER_URL + "/addLike/"; // + {{outfitId}} + / + {{userId}}
    private static final String URL_GET_IS_LIKED_OUTFIT = MainActivity.SERVER_URL + "/likedOutfit/"; // + {{outfitId}} + / + {{userId}}
    private static final String URL_PUT_SWAP_FAVORITE_ON_OUTFIT = MainActivity.SERVER_URL + "/swapFavoriteOnOutfit/"; // + {{outfitId}}

    public static final String CURRENT_OUTFIT_PARAM = "currentOutfitId";
    public static final String TOMORROW_OUTFIT_PARAM = "tomorrowOutfitId";


    public static void saveCurrentDailyOutfit(Context context, long outfitId) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CURRENT_OUTFIT_PARAM, outfitId);
        editor.apply();
    }

    public static long getCurrentDailyOutfit(Context context) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(CURRENT_OUTFIT_PARAM, -1);
    }

    public static void saveTomorrowDailyOutfit(Context context, long outfitId) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(TOMORROW_OUTFIT_PARAM, outfitId);
        editor.apply();
    }
    public static long getTomorrowDailyOutfit(Context context) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(TOMORROW_OUTFIT_PARAM, -1);
    }
    /*
    Replace with "null" for string if there is no outfit being added!!
     */


    public static void getAllOutfitsRequest(Context context, long userId,
                                            Response.Listener<JSONArray> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_ALL_USER_OUTFITS + userId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }

    public static void getOutfitRequest(Context context, long outfitId,
                                            Response.Listener<JSONObject> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_OUTFIT + outfitId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void createOutfitRequest(Context context, long userId, String name,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener) {

        JSONObject request = new JSONObject();
        try {
            request.put("userId", userId);
            request.put("outfitName", name);
            request.put("favorite", false);
        } catch (Exception e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_POST_CREATE_OUTFIT,
                request,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

//    public static void deleteOutfitRequest(Context context, long outfitId,
//                                        Response.Listener<JSONObject> responseListener,
//                                        Response.ErrorListener errorListener) {
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                Request.Method.DELETE,
//                URL_DELETE_OUTFIT + outfitId,
//                null, // Pass null as the request body since it's a GET request
//                responseListener, errorListener);
//
//        // Adding request to request queue
//        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
//    }

    public static void deleteOutfitRequest(Context context, long outfitId,
                                           Response.Listener<String> responseListener,
                                           Response.ErrorListener errorListener) {

        StringRequest StringReq = new StringRequest(
                Request.Method.DELETE,
                URL_DELETE_OUTFIT + outfitId,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(StringReq);
    }

    public static void updateOutfitRequest(Context context, JSONObject outfit,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_UPDATE_OUTFIT,
                outfit,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void getAllOutfitItemsRequest(Context context, long outfitId,
                                          Response.Listener<JSONArray> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_ALL_OUTFIT_CLOTHES + outfitId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void getAllUserClothesRequest(Context context, long userId,
                                            Response.Listener<JSONArray> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_ALL_USER_CLOTHES + userId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }

    public static void addClothingRequest(Context context, long outfitId, long clothingId,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_ADD_ITEM + outfitId + "/" + clothingId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void removeClothingRequest(Context context, long outfitId, long clothingId,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_REMOVE_ITEM + outfitId + "/" + clothingId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void addLikeRequest(Context context, long outfitId, long userId,
                                             Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_ADD_LIKE + outfitId + "/" + userId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void removeLikeRequest(Context context, long outfitId, long userId,
                                      Response.Listener<JSONObject> responseListener,
                                      Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_REMOVE_LIKE + outfitId + "/" + userId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }


    public static void isLikedOutfitRequest(Context context, long outfitId, long userId,
                                           Response.Listener<String> responseListener,
                                           Response.ErrorListener errorListener) {

        StringRequest StringReq = new StringRequest(
                Request.Method.GET,
                URL_GET_IS_LIKED_OUTFIT + outfitId + "/" + userId,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(StringReq);
    }


    public static void swapFavoriteOnOutfitRequest(Context context, long outfitId,
                                            Response.Listener<JSONObject> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_PUT_SWAP_FAVORITE_ON_OUTFIT + outfitId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
