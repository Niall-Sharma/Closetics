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
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutfitManager {

private static final String CURRENT_OUTFIT_PARAM = "currentOutfitId";
private static final String TOMORROW_OUTFIT_PARAM = "tomorrowOutfitId";


    public static void saveCurrentDailyOutfit(Context context, String outfitId) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CURRENT_OUTFIT_PARAM, outfitId);
        editor.apply();
    }

    public static String getCurrentDailyOutfit(Context context) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(CURRENT_OUTFIT_PARAM, null);
    }

    public static void saveTomorrowDailyOutfit(Context context, String outfitId) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOMORROW_OUTFIT_PARAM, outfitId);
        editor.apply();
    }
    public static String getTomorrowDailyOutfit(Context context) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOMORROW_OUTFIT_PARAM, null);
    }
    /*
    Replace with "null" for string if there is no outfit being added!!
     */





    public static void getAllOutfitsRequest(Context context, long userId, String URL,
                                            Response.Listener<JSONArray> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL + userId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }

    public static void getOutfitRequest(Context context, long outfitId, String URL,
                                            Response.Listener<JSONObject> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                URL + outfitId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void createOutfitRequest(Context context, long userId, String name, String URL,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener) {

        JSONObject request = new JSONObject();
        try {
            request.put("userId", userId);
            request.put("outfitName", name);
        } catch (Exception e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                request,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

//    public static void deleteOutfitRequest(Context context, long outfitId, String URL,
//                                        Response.Listener<JSONObject> responseListener,
//                                        Response.ErrorListener errorListener) {
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                Request.Method.DELETE,
//                URL + outfitId,
//                null, // Pass null as the request body since it's a GET request
//                responseListener, errorListener);
//
//        // Adding request to request queue
//        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
//    }

    public static void deleteOutfitRequest(Context context, long outfitId, String URL,
                                           Response.Listener<String> responseListener,
                                           Response.ErrorListener errorListener) {

        StringRequest StringReq = new StringRequest(
                Request.Method.DELETE,
                URL + outfitId,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(StringReq);
    }

    public static void updateOutfitRequest(Context context, JSONObject outfit, String URL,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                outfit,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void getAllOutfitItemsRequest(Context context, long outfitId, String URL,
                                          Response.Listener<JSONArray> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                URL + outfitId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void getAllUserClothesRequest(Context context, long userId, String URL,
                                            Response.Listener<JSONArray> responseListener,
                                            Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL + userId,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }

    public static void addClothingRequest(Context context, long outfitId, long clothingId, String URL,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL + outfitId + "/" + clothingId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }

    public static void removeClothingRequest(Context context, long outfitId, long clothingId, String URL,
                                          Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                URL + outfitId + "/" + clothingId,
                null,
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjReq);
    }


}
