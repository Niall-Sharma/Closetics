package com.example.closetics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
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

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    private int userId;
    //Postman Mock Server
    //private static final String URL = "https://803bd0f1-d0c7-422a-a0c5-06cdf909e51f.mock.pstmn.io";
   
    private static final String URL = "https://jsonplaceholder.typicode.com/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        // initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edit);
        passwordEditText = findViewById(R.id.login_password_edit);
        loginButton = findViewById(R.id.login_login_button);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_button);  // link to signup button in the Login activity XML

        //Login Button
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //Login request to the backend API
            UserManager.loginRequest(getApplicationContext(), username, password, URL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Response is a JSON object from the backend end
                            try {
                                String token = response.getString("token");//This grabs the string value of JSON header
                                UserManager.saveLoginToken(getApplicationContext(), token);
                                //Username???

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null){
                                int statusCode = error.networkResponse.statusCode;
                                String errorBody = null;

                                //HttpStatus.UNAUTHORIZED
                                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                                    try{
                                        errorBody = new String (error.networkResponse.data, "UTF-8");
                                        Log.d("Error", "401 Unauthorized");

                                    }catch(UnsupportedEncodingException e){
                                        e.printStackTrace();
                                    }
                                }
                                //More
                            }
                        }
                    });

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("PASSWORD", password);
            startActivity(intent);
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }

        });
    }




}