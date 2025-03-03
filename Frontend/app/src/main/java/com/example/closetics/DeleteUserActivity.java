package com.example.closetics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

//import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class DeleteUserActivity extends AppCompatActivity {

    private static final String URL_DELETE_USER = "http://10.0.2.2:8080/users/"; // +{{id}}
    private static final String URL_GET_USER_BY_USERNAME = "http://10.0.2.2:8080/users/username/"; // +{{username}}

    private Button yesButton;
    private Button noButton;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        yesButton = findViewById(R.id.delete_user_yes_button);
        noButton = findViewById(R.id.delete_user_no_button);
        errorText = findViewById(R.id.delete_user_error_text);

        errorText.setVisibility(TextView.GONE); // hide error message by default

        yesButton.setOnClickListener(v -> {
            deleteUser(UserManager.getUsername(getApplicationContext()));
        });

        noButton.setOnClickListener(v -> {
            // return to MainActivity
            Intent intent = new Intent(DeleteUserActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }

    private void deleteUser(String username) {
        UserManager.deleteUserRequest(getApplicationContext(), username, URL_DELETE_USER, URL_GET_USER_BY_USERNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Successful Delete: " + response.toString());

                        // remove token and username from shared prefs
                        UserManager.saveLoginToken(getApplicationContext(), null);
                        UserManager.saveUsername(getApplicationContext(), null);

                        // return to MainActivity
                        Intent intent = new Intent(DeleteUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Deletion Error: " + error.toString());

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            setErrorMessage("Connection timeout");
                        }
                        else if (error.networkResponse != null) {
                            setErrorMessage("Error Code: " + error.networkResponse.statusCode);
                            Log.e("Volley Error", "Error Code: " + error.networkResponse.statusCode);
                        }
                        else {
                            setErrorMessage("Unknown deletion error");
                        }
                    }
                });
    }
}