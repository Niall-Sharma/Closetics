package com.example.closetics;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OutfitManager {
    public static void getOutfitsRequest(Context context, long userId, String URL,
                                  Response.Listener<JSONArray> responseListener,
                                  Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null, // Pass null as the request body since it's a GET request
                responseListener, errorListener);

        // Adding request to request queue
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrReq);
    }
}
