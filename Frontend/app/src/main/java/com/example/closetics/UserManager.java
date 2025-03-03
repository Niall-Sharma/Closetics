package com.example.closetics;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class UserManager {

    private static final String SHARED_PREFERENCES_FILE_NAME = "CloseticsPreferences";
    private static final String TOKEN_PARAM = "logInToken";
    private static final String USERNAME_PARAM = "username";
    private static final String USER_ID_PARAM = "userID";
    private static final ArrayList<String> SECURITY_QUESTIONS= new ArrayList<>(Arrays.asList(
    "What is your mother's maiden name?", "What was the name of your first pet?",
            "What is the name of the street you grew up on?" ,"What is your favorite color?", "What was the name of your first school?", "What was your childhood nickname?"
            , "In what city were you born?", "What is your favorite food?", "What was your first car?", "What is the name of your childhood best friend?"));



    //These methods store the data in sharedPreferences
    //Shared preferences used when a value needs to persist across app sessions

    public static void saveLoginToken(Context context, String token) {
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_PARAM, token);
        editor.apply();
    }

    public static String getLoginToken(Context context) {
        //Access the shared preferences file make it private to this app
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
        //Access the shared preferences file make it private to this app
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME_PARAM, null);
    }

    //**** Might need to store these as ints for the backend ****
    public static String getUserID(Context context ){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USER_ID_PARAM, null);
    }

    public static void saveUserID(Context context, String userID){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID_PARAM, userID);
        editor.apply();
    }


    public static ArrayList<String> getSecurityQuestions(){
        return SECURITY_QUESTIONS;
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
    public static void signupRequest(Context context, String username, String email, String password, String securityAnswer1,
                                     String securityAnswer2, int securityQuestion1, int securityQuestion2,
                                     String URL,
                                    Response.Listener<JSONObject> responseListener,
                                    Response.ErrorListener errorListener) {

        //Create the json object of the login data (username and password)
        JSONObject signupData = new JSONObject();

        //Use try catch blocks when creating JSON objects
        try {
            signupData.put("username", username);
            signupData.put("email", email);
            signupData.put("password", password);
            //Add checks for if these are null/blank? Do we want to require them?
            signupData.put("sQA1", securityAnswer1);
            signupData.put("sQID1", securityQuestion1);
            signupData.put("sQA2", securityAnswer2);
            signupData.put("sQID2", securityQuestion2);
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


    public static void changePasswordRequest(Context context, String newPassword, String securityInput, String URL,
                                             Response.Listener<JSONObject> responseListener,
                                             Response.ErrorListener errorListener) {
        //Create the json object of the login data (username and password)
        JSONObject updatePasswordData = new JSONObject();

        //Use try catch blocks when creating JSON objects
        try {
            updatePasswordData.put("newPassword", newPassword);
            updatePasswordData.put("securityQuestionID", securityInput);
        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
            return;
        }


        //The post request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                updatePasswordData, responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    /*
    This request needs to be updated once backend is updated
    */
    public static void editUserRequest(Context context, String userid, String newUsername, String newEmail, String URL,
                                           Response.Listener<JSONObject> responseListener,
                                           Response.ErrorListener errorListener){

        JSONObject updateUsernameData = new JSONObject();

        try{
            //Turn id into a long for backend
            long id = Long.parseLong(userid);
            updateUsernameData.put("userId", id);
            //Do not send if null
            if (newUsername!=null){
                updateUsernameData.put("username", newUsername);
            }
            if (newEmail!= null) {
                updateUsernameData.put("newEmail", newEmail);
            }

        } catch (Exception e) {
            Log.e("JSON Error", e.toString());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                updateUsernameData, responseListener, errorListener);

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
                                     Response.Listener<String> responseListener,
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
                errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    public static void deleteUserRequest(Context context, long id, String URL,
                                         Response.Listener<String> responseListener,
                                         Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                URL + id, // add id to the URL
                responseListener, errorListener);
        //Add request to the volley singleton request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}
