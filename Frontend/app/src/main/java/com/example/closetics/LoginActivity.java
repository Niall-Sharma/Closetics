package com.example.closetics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        // initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edit);
        passwordEditText = findViewById(R.id.login_password_edit);
        loginButton = findViewById(R.id.login_login_button);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_button);  // link to signup button in the Login activity XML

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
}