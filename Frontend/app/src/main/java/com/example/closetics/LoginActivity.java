package com.example.closetics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//10.0.2.2
public class LoginActivity extends AppCompatActivity {

    //Login functionality: Post requests to the backend

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private Button forgotPasswordButton;  //define forgotPassword button variable
    private TextView errorText;
    //To be sent to the forgot password fragment
    private long id1;
    private long id2;
    private long userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        // initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edit);
        passwordEditText = findViewById(R.id.login_password_edit);
        loginButton = findViewById(R.id.login_login_button);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_button);  // link to signup button in the Login activity XML
        forgotPasswordButton = findViewById(R.id.login_forgot_password); //link to forgot password button in Login activity XML
        errorText = findViewById(R.id.login_error_text);

        errorText.setVisibility(TextView.GONE); // hide error message by default


        //Login Button
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //Login request to the backend API
            UserManager.loginRequest(getApplicationContext(), username, password,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Response is a JSON object from the backend end

                            //Check if valid request, that is the only error possible onResponse
                           String message = null;
                            try {
                                 message = response.getString("message");
                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                            }


                            if (message.equals("Invalid Request")){
                                Log.e("Volley Error", "Invalid Request");
                                setErrorMessage("Please provide both a username and email");

                            }
                            //Successful login
                            else {
                                Log.d("Volley Response", "Successful Login: " + response.toString());
                                try {
                                    String token = response.getString("token");//This grabs the string value of token JSON header
                                    long userID = response.getLong("user_id");
                                    //Save userID in shared preferences
                                    UserManager.saveUserID(getApplicationContext(), userID);
                                    //Save session token in shared preferences
                                    UserManager.saveLoginToken(getApplicationContext(), token);
                                    //Save username in shared preferences
                                    UserManager.saveUsername(getApplicationContext(), username);
                                    //Save userTier in shared preferences
                                    saveUserTier(userID);

                                    // start websocket for recommendations
                                    startRecWebSocket();

                                } catch (JSONException e) {
                                    Log.e("JSON Error", e.toString());

                                }
                                //Switch to main page on success
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("USERNAME", username);
                                intent.putExtra("PASSWORD", password);
                                startActivity(intent);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.toString());

                            //Handling timeout errors and unknown errors from Volley error
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                setErrorMessage("Connection timeout");
                                return;
                            }
                            else if (error.networkResponse == null) {
                                setErrorMessage("Unknown error");
                                return;
                            }

                            //Grab the status code ex.
                            int statusCode = error.networkResponse.statusCode;

                            //500 error
                            if (statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { //500 error
                                Log.e("Volley Error", "Error 500:  (" + error.toString() + ")"); //"An error occurred during logging in"
                                setErrorMessage(error.toString() + " , Please try again!");

                            }
                            //401 error
                            else{
                                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){ //401 error
                                    Log.e("Volley Error", "Error 401: Invalid username, email or password  (" + error.toString() + ")");
                                    setErrorMessage("Invalid username, email or password");

                                }

                            }

                        }
                    });
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }

        });
        //Forgot password onClick logic
        //This click will bring up the fragment
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                if (username.isEmpty()){
                    setErrorMessage("Please enter your username to change password");
                } else {
                    Log.d("Volley Debug", "Making request to get SQ IDs of user " + username);
                    getSecurityQuestionIDs(getApplicationContext(), username);
                    // Do not allow any more button presses while waiting
                    forgotPasswordButton.setEnabled(false);
                }
            }
        });
    }


    private void showFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //New instance of forgotpassword fragment containing the id instance variables
        Fragment fragment = ForgotPasswordFragment.newInstance(id1, id2, userId);
        transaction.replace(R.id.forgot_password_fragment_container, fragment, "forgot_password_fragment");
        transaction.commit();
        Log.d("Fragment debug", String.valueOf(fragment.isAdded()));

    }
    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }


    //Get request to grab which security questions that were answered also grabs the userID
    private void getSecurityQuestionIDs(Context context, String username) {
        // Create a JsonObjectRequest for the GET request
        UserManager.getUserByUsernameRequest(context, username,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", "Received JSON: " + response.toString());
                            // Retrieve the two IDs from the response
                            id1 = response.getLong("sQID1");  // Assuming the first ID is named "id1"
                            id2 = response.getLong("sQID2");// Assuming the second ID is named "id2"
                            userId = response.getLong("userId");
                            //show fragment only on response!!
                            showFragment();
                            //Reset button functionality
                            forgotPasswordButton.setEnabled(true);

                        } catch (JSONException e) {
                            Log.e("Volley Error", "Error in user/username JSON");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        // Handle error
                        Log.e("Volley Error", error.toString());
                        setErrorMessage("That username does not exist, please enter valid username");
                        forgotPasswordButton.setEnabled(true);
                    }
                });
    }

    private void saveUserTier(long userId) {
        UserManager.getUserByIdRequest(getApplicationContext(), userId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", "Successful User Tier Save: " + response.toString());

                            String userTier = response.getString("userTier");
                            if (userTier == null) userTier = "Free";
                            if ("premium".equalsIgnoreCase(userTier)) {
                                UserManager.saveUserTier(getApplicationContext(), 2);
                            } else if ("basic".equalsIgnoreCase(userTier)) {
                                UserManager.saveUserTier(getApplicationContext(), 1);
                            } else { // free
                                UserManager.saveUserTier(getApplicationContext(), 0);
                            }
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get user by ID Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get user by ID Error: " + error.toString());
                    }
                });
    }

    private void startRecWebSocket() {
        Intent serviceIntent = new Intent(this, com.example.closetics.recommendations.RecWebSocketService.class);
        serviceIntent.setAction("RecWebSocketConnect");
        startService(serviceIntent);
        Log.d("LoginActivity", "RecWebSocket started");
    }


}