package com.example.closetics.dashboard;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class StatisticsManager {




    /*
    Outfit stats section
     */
    public static void getOutfitsStatsRequest(Context context, long outfitId, String URL, Response.Listener<JSONObject> responseListener,
                                              Response.ErrorListener errorListener){


        String getUrl = URL + "/getOutfitStats/" + outfitId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }
    //Put mapping
    public static void addWornOutfitTodayRequest(Context context, long outfitId, String URL, Response.Listener<JSONObject> responseListener,
                                          Response.ErrorListener errorListener){
        String putUrl = URL + "/wornOutfitToday/" + outfitId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                putUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }

    public static void mostExpensiveOutfitRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                                  Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersMostExpensiveOutfit/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);


    }
    public static void mostWornOutfitRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersMostWornOutfit/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);


    }

    public static void coldestAverageOutfitRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersColdestAvgOutfit/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);


    }

    public static void warmestAverageOutfitRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                                   Response.ErrorListener errorListener) {

        String getUrl = URL + "/getUsersWarmestAvgOutfit/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl,  null,responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /*
    Clothing stats requests
     */
    public static void getClothingStatsRequest(Context context, long clothingId, String URL, Response.Listener<JSONObject> responseListener,
                                               Response.ErrorListener errorListener) {

        String getUrl = URL + "/getClothingStats/" + clothingId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void addWornClothingTodayRequest(Context context, long clothingId, String URL, Response.Listener<JSONObject> responseListener,
                                            Response.ErrorListener errorListener){

        String putUrl = URL + "/wornClothingToday/" + clothingId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                putUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
    public static void calcNumberOfOutfitsInRequest(Context context, long clothingId, String URL, Response.Listener<String> responseListener,
                                             Response.ErrorListener errorListener){

        String putUrl = URL + "/numberOfOutfitsIn/" + clothingId;

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                putUrl,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void mostExpensiveClothingRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                                    Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersMostExpensiveClothing/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void mostWornClothingRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                               Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersMostWornClothing/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void coldestAverageClothingRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                                     Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersColdestAvgClothing/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void warmestAverageClothingRequest(Context context, long userId, String URL, Response.Listener<JSONObject> responseListener,
                                                     Response.ErrorListener errorListener){

        String getUrl = URL + "/getUsersWarmestAvgClothing/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getUrl,
                null,
                responseListener,
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }







}
