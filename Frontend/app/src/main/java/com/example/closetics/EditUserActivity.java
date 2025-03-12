package com.example.closetics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.InputMismatchException;

public class EditUserActivity extends AppCompatActivity {


    private EditText newUsername;
    private EditText newEmail;


    private String username;
    private String email;

    private Button submit;
    private Button cancel;
    private TextView errorText;
    private final String URL = MainActivity.SERVER_URL + "/updateUser";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        newUsername = findViewById(R.id.change_username_edit);
        newEmail = findViewById(R.id.change_email_new_edit);
       submit = findViewById(R.id.edit_user_submit);
        cancel = findViewById(R.id.edit_user_cancel);
        errorText = findViewById(R.id.edit_error_text);

        errorText.setVisibility(TextView.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMainActivity();
            }
        });


        /*
        This method still needs updated (error implementation for request and when usernames don't match)
         */
        //Small issue, on signup userID doesn't get updated!!!
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = newUsername.getText().toString();
                email = newEmail.getText().toString();
                //Do not continue
                if (username.isEmpty() && email.isEmpty()){
                    Log.d("User error", "No field updated");
                    setErrorMessage("Please enter either a new email, new username, or both");
                }
                else {
                if (username.isEmpty()){
                    username = null;
                }
                if (email.isEmpty()){
                    email = null;
                }
                    UserManager.editUserRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()), username, email, URL,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Overwrite new username in shared preferences

                                    Log.d("Volley response", "Succesful edit user" + response.toString());
                                    if (username != null) {
                                        //overwrite username
                                        UserManager.saveUsername(getApplicationContext(), username);
                                    }

                                    returnMainActivity();

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Add boilerplate volley errors
                                    Log.e("Volley Error", "Edit user error: " + error.toString());

                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        setErrorMessage("Connection timeout");
                                    } else if (error.networkResponse != null) {
                                        setErrorMessage("Error Code: " + error.networkResponse.statusCode);
                                        Log.e("Volley Error", "Error Code: " + error.networkResponse.statusCode);
                                    } else {
                                        setErrorMessage("Unknown edit user error");
                                    }
                                    int statusCode = error.networkResponse.statusCode;

                                    if (statusCode == HttpURLConnection.HTTP_CONFLICT) {
                                        Log.e("Volley Error", "Error 409:  (" + error.toString() + ")"); //"A conflict occurred with email or username"
                                        setErrorMessage("Username or email already in use, please choose another one");
                                    }
                                }
                            });
                }


                }

        });


    }

    private void returnMainActivity(){
        Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);

    }
    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }

}
