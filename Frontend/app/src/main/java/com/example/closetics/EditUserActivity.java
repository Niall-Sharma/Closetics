package com.example.closetics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.InputMismatchException;

public class EditUserActivity extends AppCompatActivity {


    private EditText initialUsername;
    private EditText confirmUsername;

    private String username;

    private Button submit;
    private Button cancel;
    private final String URL = "https://baacab8f-1ecd-41d2-b30f-cc9889421d1d.mock.pstmn.io/updateUser";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        initialUsername = findViewById(R.id.change_username_edit);
        confirmUsername = findViewById(R.id.change_username_confirm_edit);
        submit = findViewById(R.id.edit_user_submit);
        cancel = findViewById(R.id.edit_user_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMainActivity();
            }
        });


        /*
        This method still needs updated (error implementation for request and when usernames don't match)
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isRunning = true;
                setUsername(isRunning);
                //Request if setUsername worked!!
                if (isRunning){
                    String currentUsername = UserManager.getUsername(getApplicationContext());
                    UserManager.editUsernameRequest(getApplicationContext(), currentUsername, username, URL,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Overwrite new username in shared preferences
                                    String newUsername = null;
                                    try {
                                        newUsername = response.get("username").toString();
                                        UserManager.saveUsername(getApplicationContext(), newUsername);

                                    } catch (JSONException e) {
                                        Log.d("JSON error", e.toString());
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Add functionality
                                }
                            });
                   returnMainActivity();

                }



                //Add functionality for when the usernames do not match in the edit text fields
            }


        });





    }

    private void returnMainActivity(){
        Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
        startActivity(intent);

    }
    private void setUsername(Boolean isRunning){
        if (initialUsername.getText().toString().equals(confirmUsername.getText().toString())){
            username = initialUsername.getText().toString();
        }
        else{
            //Do not continue with the request!!
            isRunning = false;
        }
    }
}
