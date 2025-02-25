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
    private Button forgotPasswordButton;  //define forgotPassword button variable




    //Postman Mock Server
    private static final String URL = "https://baacab8f-1ecd-41d2-b30f-cc9889421d1d.mock.pstmn.io/login";

   // private static final String URL = "https://jsonplaceholder.typicode.com/login";

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
                                String token = response.getString("token");//This grabs the string value of token JSON header
                                //Save session token in shared preferences
                                UserManager.saveLoginToken(getApplicationContext(), token);
                                //Save username in shared preferences
                                UserManager.saveUsername(getApplicationContext(), username);

                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                                return;
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
                                        Log.e("JSON Error", e.toString());
                                        return;
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
        //Forgot password onClick logic
        //This click will bring up the fragment
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment();
            }
        });


    }
    private void showFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new ForgotPasswordFragment();
        transaction.replace(R.id.forgot_password_fragment_container, fragment, "forgot_password_fragment");
        transaction.commit();

    }




}