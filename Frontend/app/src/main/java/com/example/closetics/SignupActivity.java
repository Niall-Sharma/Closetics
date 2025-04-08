package com.example.closetics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String URL_SIGNUP = MainActivity.SERVER_URL + "/signup";
    private static final String URL_GET_USER_BY_USERNAME = MainActivity.SERVER_URL + "/users/username/"; // +{{username}}

    private EditText usernameEditText;  // define username edittext variable
    private EditText emailEditText;  // define email edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;// define confirm edittext variable
    private EditText securityAnswerEditText1;
    private EditText securityAnswerEditText2;
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private TextView errorText;
    private Spinner securityAnswerSpinner1;
    private Spinner securityAnswerSpinner2;

    private int securityQuestion1;
    private int securityQuestion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        securityAnswerSpinner1 = findViewById(R.id.signup_sq1_spinner);
        securityAnswerSpinner2 = findViewById(R.id.signup_sq2_spinner);
        securityAnswerEditText2= findViewById(R.id.editTextText2);
        securityAnswerEditText1 = findViewById(R.id.signup_sq1_edit);
        usernameEditText = findViewById(R.id.signup_username_edit);
        emailEditText = findViewById(R.id.signup_email_edit);
        passwordEditText = findViewById(R.id.signup_password_edit);
        confirmEditText = findViewById(R.id.signup_confirm_edit);
        loginButton = findViewById(R.id.signup_login_button);
        signupButton = findViewById(R.id.signup_signup_button);
        errorText = findViewById(R.id.signup_error_text);

        errorText.setVisibility(TextView.GONE); // hide error message by default

        //Set up spinners
        ArrayList<String> securityAnswerItems = UserManager.getSecurityQuestions();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, securityAnswerItems);
        securityAnswerSpinner1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, securityAnswerItems);
        securityAnswerSpinner2.setAdapter(adapter2);



        //Save the selected question to send in request
        securityAnswerSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String chosenQuestion = adapter1.getItem(position);
                    securityQuestion1 = position;

            }

            //First element in list when nothing selected
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    String chosenQuestion = securityAnswerItems.get(0);
                    securityQuestion1 = 0;
            }
            });


        securityAnswerSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String chosenQuestion = adapter2.getItem(position);
                securityQuestion2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String chosenQuestion = securityAnswerItems.get(0);
                securityQuestion2 = 0;
            }
        });





        // On click listeners
        loginButton.setOnClickListener(v -> {
            // Switch to Login activity
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {

            /* grab strings from user inputs */
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirm = confirmEditText.getText().toString().trim();
            String securityAnswer1 = securityAnswerEditText1.getText().toString().trim();
            String securityAnswer2 = securityAnswerEditText2.getText().toString().trim();

            //Check if the security questions were the same
            if (securityQuestion2 == securityQuestion1){
                setErrorMessage("Please choose two different security questions");
                return;
            }

            //Check if security questions were answered
            if (securityAnswer1.isEmpty() || securityAnswer2.isEmpty()){
                setErrorMessage("Please answer the security questions");
                return;
            }

            if (!password.equals(confirm)) {
                setErrorMessage("Passwords must match");
                return;
            }

            //Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_SHORT).show();

            signUp(username, email, password, securityAnswer1, securityAnswer2);
        });
    }

    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }

    private void signUp(String username, String email, String password, String securityAnswer1, String securityAnswer2) {
        UserManager.signupRequest(getApplicationContext(), username, email, password, securityAnswer1, securityAnswer2, securityQuestion1, securityQuestion2, URL_SIGNUP,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("Error")) { // handle error if it is present
                                String errorMessage = response.getString("Error");
                                Log.e("Volley Error", errorMessage);

                                // currently the oly error possible here is: username, email, or password is empty
                                setErrorMessage("Please enter username, email, and password");

                                return;
                            }

                            Log.d("Volley Response", "Successful Signup: " + response.toString());

                            // save token and username to shared prefs
                            String token = response.getString("token");
                            UserManager.saveLoginToken(getApplicationContext(), token);
                            UserManager.saveUsername(getApplicationContext(), username);

                            saveUserId(username);

                            // return to MainActivity after successful signup and login
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", e.toString());
                        }

//                        if (errorMessage != null) { // username, email, or password is empty
//                            setErrorMessage("Please enter username, email, and password");
//                            return;
//                        }
//
//                        // log in the user if signup was successful
//                        login(username, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            setErrorMessage("Connection timeout");
                            return;
                        }
                        else if (error.networkResponse == null) {
                            setErrorMessage("Unknown error");
                            return;
                        }
                        int statusCode = error.networkResponse.statusCode;
                        String errorBody = "";

                        // display error
                        if (statusCode == HttpURLConnection.HTTP_CONFLICT) {
                            Log.e("Volley Error", "Error 409: Username already exists (" + error.toString() + ")");
                            setErrorMessage("A user with this username already exits");
                        }
                        else if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            Log.e("Volley Error", "Error 401: Invalid username or password or email (" + error.toString() + ")");

                            // get the error body (error message)
                            try {
                                if (error.networkResponse.data != null) {
                                    errorBody = new String(error.networkResponse.data, "UTF-8");
                                }
                            }
                            catch(UnsupportedEncodingException e) {
                                Log.e("Encoding Error", e.toString());
                            }

                            // display correct error message
                            if (errorBody.toLowerCase().contains("username")) { // invalid username
                                setErrorMessage("Invalid username\n" +
                                        "Username must be 3-16 characters long and\n" +
                                        "contain only letters and numbers");
                            }
                            else if (errorBody.toLowerCase().contains("email")) { // invalid email
                                setErrorMessage("Invalid email");
                            }
                            else { // invalid password
                                setErrorMessage("Invalid password\n" +
                                        "Password must be at least 8 characters long and\n" +
                                        "contain a lower case letter, an upper case letter,\n" +
                                        "a digit, a special symbol (@#$%^&+=), no spaces");
                            }
                        }
                        else {
                            Log.e("Volley Error", "Error: " + statusCode);
                            setErrorMessage("Error code: " + statusCode);
                        }
                    }
                });
    }

    private void saveUserId(String username) {
        UserManager.getUserByUsernameRequest(getApplicationContext(), username, URL_GET_USER_BY_USERNAME,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", "Successful User ID Save: " + response.toString());

                            long id = response.getLong("userId");
                            UserManager.saveUserID(getApplicationContext(), id);

                            // start websocket for recommendations
                            startRecWebSocket();
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get user by username Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get user by username Error: " + error.toString());

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            setErrorMessage("Connection timeout");
                        }
                        else if (error.networkResponse != null) {
                            setErrorMessage("Error Code: " + error.networkResponse.statusCode);
                            Log.e("Volley Error", "Error Code: " + error.networkResponse.statusCode);
                        }
                        else {
                            setErrorMessage("Unknown Get user by username Error");
                        }
                    }
                });
    }

    private void startRecWebSocket() {
        Intent serviceIntent = new Intent(this, com.example.closetics.recommendations.RecWebSocketService.class);
        serviceIntent.setAction("RecWebSocketConnect");
        startService(serviceIntent);
        Log.d("SignupActivity", "RecWebSocket started");
    }

//    private void login(String username, String password) {
//        UserManager.loginRequest(getApplicationContext(), username, password, URL_LOGIN,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("Volley Response", "Successful Login: " + response.toString());
//
//                            // save token and username to shared prefs
//                            String token = response.getString("token");
//                            UserManager.saveLoginToken(getApplicationContext(), token);
//                            UserManager.saveUsername(getApplicationContext(), username);
//
//                            // return to MainActivity after successful signup and login
//                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//                            startActivity(intent);
//                        }
//                        catch (JSONException e) {
//                            Log.e("JSON Error", e.toString());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
//
//                        if (error.networkResponse == null) {
//                            setErrorMessage("Unknown login error");
//                            return;
//                        }
//                        int statusCode = error.networkResponse.statusCode;
//
//                        if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) { // Invalid username or password
//                            Log.e("Volley Error", "Error 401: Invalid username or password");
//                            try {
//                                String errorBody = new String(error.networkResponse.data, "UTF-8");
//                                setErrorMessage(errorBody);
//                            }
//                            catch(UnsupportedEncodingException e){
//                                Log.e("Encoding Error", e.toString());
//                            }
//                        }
//                        else {
//                            Log.e("Volley Error", "Error " + statusCode);
//                            setErrorMessage("Error code: " + statusCode);
//                        }
//                    }
//                });
//    }
}