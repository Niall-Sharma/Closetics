package com.example.closetics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String URL_SIGNUP = "http://10.0.2.2:8080/signup";
    private static final String URL_LOGIN = "http://10.0.2.2:8080/login";
    private static final String URL_DELETE_USER = "http://10.0.2.2:8080/users/"; // +{{id}}

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.signup_username_edit);
        passwordEditText = findViewById(R.id.signup_password_edit);
        confirmEditText = findViewById(R.id.signup_confirm_edit);
        loginButton = findViewById(R.id.signup_login_button);
        signupButton = findViewById(R.id.signup_signup_button);
        errorText = findViewById(R.id.signup_error_text);

        errorText.setVisibility(TextView.GONE); // hide error message by default

        // On click listeners
        loginButton.setOnClickListener(v -> {
            // Switch to Login activity
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {

            /* grab strings from user inputs */
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirm = confirmEditText.getText().toString().trim();

            if (!password.equals(confirm)){
                setErrorMessage("Passwords must match");
                return;
            }

            Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_SHORT).show();

            signUp(username, password);
        });
    }

    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }

    private void signUp(String username, String password) {
        UserManager.signupRequest(getApplicationContext(), username, password, URL_SIGNUP,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful Signup: " + response.toString());

                        // log in the user if signup was successful
                        login(username, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());

                        if (error.networkResponse == null) {
                            setErrorMessage("Unknown error");
                            return;
                        }
                        int statusCode = error.networkResponse.statusCode;

                        // display error
                        if (statusCode == HttpURLConnection.HTTP_CONFLICT) {
                            Log.e("Volley Error", "Error 409: Username already exists");
                            setErrorMessage("A user with this username already exits");
                        }
                        else if (statusCode == HttpURLConnection.HTTP_NOT_ACCEPTABLE) {
                            Log.e("Volley Error", "Error 406: Invalid username or password");
                            if (!UserManager.validateUsername(username)) { // invalid username
                                setErrorMessage("Invalid username\n" +
                                        "Username must be 3-16 characters long and\n" +
                                        "contain only letters and numbers");
                            }
                            else { // invalid password
                                setErrorMessage("Invalid password\n" +
                                        "Password must be at least 8 characters long and\n" +
                                        "contain a lower case letter, an upper case letter,\n" +
                                        "a digit, a special symbol (@#$%^&+=), no spaces");
                            }
                        }
                        else {
                            Log.e("Volley Error", "Error " + statusCode);
                            setErrorMessage("Error code: " + statusCode);
                        }
                    }
                });
    }

    private void login(String username, String password) {
        UserManager.loginRequest(getApplicationContext(), username, password, URL_LOGIN,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", "Successful Login: " + response.toString());

                            // save token and username to shared prefs
                            String token = response.getString("token");
                            UserManager.saveLoginToken(getApplicationContext(), token);
                            UserManager.saveUsername(getApplicationContext(), username);

                            // return to MainActivity after successful signup and login
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());

                        if (error.networkResponse == null) {
                            setErrorMessage("Unknown login error");
                            return;
                        }
                        int statusCode = error.networkResponse.statusCode;
                        String errorBody;

                        if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) { // Invalid username or password
                            Log.e("Volley Error", "Error 401: Invalid username or password");
                            try {
                                errorBody = new String(error.networkResponse.data, "UTF-8");
                                setErrorMessage(errorBody);
                            }
                            catch(UnsupportedEncodingException e){
                                Log.e("Encoding Error", e.toString());
                            }
                        }
                        else {
                            Log.e("Volley Error", "Error " + statusCode);
                            setErrorMessage("Error code: " + statusCode);
                        }
                    }
                });
    }
}