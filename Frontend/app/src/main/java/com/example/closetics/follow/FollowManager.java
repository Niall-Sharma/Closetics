package com.example.closetics.follow;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.closetics.MainActivity;
import com.example.closetics.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Helper class with Follow-related HTTP requests.
 */
public class FollowManager {
    private static final String URL_GET_ALL_USER_CLOTHES = MainActivity.SERVER_URL + "/getClothing/user/"; // + {{userId}}


    /**
     *
     * @param context
     * @param userId
     * @param responseListener
     * @param errorListener
     */
    public static void getAllUserClothesRequest(Context context, long userId,
                                                Response.Listener<JSONArray> responseListener,
                                                Response.ErrorListener errorListener) {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_ALL_USER_CLOTHES + userId,
                null, responseListener, errorListener);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
