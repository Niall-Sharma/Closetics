package com.example.closetics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final String SHARED_PREFERENCES_FILE_NAME = "CloseticsPreferences";
    private static final String TOKEN_PARAM = "logInToken";
    private static final String USERNAME_PARAM = "username";

    public static void saveLoginToken(Context context, String token) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_PARAM, token);
        editor.apply();
    }

    public static String getLoginToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_PARAM, null);
    }

    public static void saveUsername(Context context, String username) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME_PARAM, username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME_PARAM, null);
    }

    public static boolean validateUsername(String username){
        String pattern = "[0-9A-Za-z]{3,16}";
        return username.matches(pattern);
    }

    /**
     * This uses a post request to send the user's input for username and password so that
     * authentication is handled in the backend versus the frontend (more secure than get requests
     * to the frontend)
     *
     * @param context
     * @param username
     * @param password
     * @param URL
     * @param responseListener
     * @param errorListener
     */
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
            Log.e("JSON Error", e.toString());
            return;
        }


        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                loginData, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * This uses a post request to create a new user with given username and password
     *
     * @param context
     * @param username
     * @param email
     * @param password
     * @param URL
     * @param responseListener
     * @param errorListener
     */
    public static void signupRequest(Context context, String username, String email, String password, String URL,
                                    Response.Listener<JSONObject> responseListener,
                                    Response.ErrorListener errorListener) {

        //Create the json object of the login data (username and password)
        JSONObject signupData = new JSONObject();

        //Use try catch blocks when creating JSON objects
        try {
            signupData.put("username", username);
            signupData.put("email", email);
            signupData.put("password", password);
        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
            return;
        }


        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                signupData, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * This uses a post request to create a new user with given username and password
     *
     * @param context
     * @param username
     * @param URL
     * @param responseListener
     * @param errorListener
     */
    public static void deleteUserRequest(Context context, String username, String URL, String getUserByUsernameURL,
                                     Response.Listener<JSONObject> responseListener,
                                     Response.ErrorListener errorListener) {

        // use GET request to getUserByUsernameURL to get the user's id
        // to then delete user by id
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getUserByUsernameURL + username, // add username to the URL
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            long id = response.getLong("userId");

                            // call delete by id method
                            UserManager.deleteUserRequest(context, id, URL, responseListener, errorListener);
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get user by username Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Volley Error", "Get user by username Error, Code: " + error.networkResponse.statusCode);
                        }
                        else {
                            Log.e("Volley Error", "Unknown Get user by username Error");
                        }
                    }
                });
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void deleteUserRequest(Context context, long id, String URL,
                                         Response.Listener<JSONObject> responseListener,
                                         Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                URL + id, // add id to the URL
                null, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
