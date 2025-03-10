package com.example.closetics.outfits;

import android.content.Context;
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
