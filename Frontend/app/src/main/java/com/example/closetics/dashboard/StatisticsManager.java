package com.example.closetics.dashboard;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class StatisticsManager {




    public static void getOutfitsStatsRequest(Context context, long outfitId, String URL, Response.Listener<JSONObject> responseListener,
                                              Response.ErrorListener errorListener){


        String getUrl = URL + "/getOutfitStats/" + outfitId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl, null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);

    }





}
