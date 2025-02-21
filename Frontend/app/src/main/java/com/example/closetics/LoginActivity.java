package com.example.closetics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    //Postman Mock Server
    private static final String URL = "https://803bd0f1-d0c7-422a-a0c5-06cdf909e51f.mock.pstmn.io";

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

    //This uses a post request to send the user's input for username and password so that
    //authentication is handled in the backend versus the frontend (more secure than get requests
    // to the frontend)
    private void loginRequest(){

        //Create the json object of the login data (username and password)
        JSONObject loginData = new JSONObject();

        //Use try catch blocks when creating JSON objects
        try{
            loginData.put();
            //Use proper headers for the backend as string,
            // int will be the actual data (username/password)
            loginData.put();
        }
        catch(JSONException e){
            e.printStackTrace();
        }


        //The push request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.Post,
                URL,
                loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //Add request to the volley singleton request queue




    }
}