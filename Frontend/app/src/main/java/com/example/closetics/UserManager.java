package com.example.closetics;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class UserManager {

    private static final String SHARED_PREFERENCES_FILE_NAME = "CloseticsPreferences";


    //This uses a post request to send the user's input for username and password so that
    //authentication is handled in the backend versus the frontend (more secure than get requests
    // to the frontend)
    public static void loginRequest(Context context, String username, String password, String URL,
                                     Response.Listener<JSONObject> responseListener,
                                     Response.ErrorListener errorListener) {

        //Create the json object of the login data (username and password)
        JSONObject loginData = new JSONObject();

        //Use try catch blocks when creating JSON objects
        try {
            loginData.put("username", username);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                loginData, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
    //This method stores the login token in sharedPreferences (standard)
    //Shared preferences used when a value needs to persist across app sessions

    //CloseticsPreferences is now the name of the shared preference file!
    public static void saveLoginToken(Context context, String token) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("logInToken", token);
        editor.apply();

    }



}



