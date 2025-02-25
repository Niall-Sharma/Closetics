package com.example.closetics;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.InputMismatchException;

public class EditUserActivity extends AppCompatActivity {


    private EditText newUsername;
    private EditText confirmUsername;
    private String username;
    private Button submit;
    private Button cancel;
    private final String URL = "https://baacab8f-1ecd-41d2-b30f-cc9889421d1d.mock.pstmn.io/updateUser";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        newUsername = findViewById(R.id.change_username_edit);
        confirmUsername = findViewById(R.id.change_username_confirm_edit);
        submit = findViewById(R.id.edit_user_submit);
        cancel = findViewById(R.id.edit_user_cancel);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isRunning = true;
                setUsername(isRunning);
                //Request if setUsername worked!!
                if (isRunning){

                }
                //Add functionality for when it doesn't!!
            }


        });





    }
    private void setUsername(Boolean isRunning){
        if (newUsername.getText().toString().equals(confirmUsername.getText().toString())){
            username = newUsername.getText().toString();
        }
        else{
            //Do not continue with the request!!
            isRunning = false;
        }
    }
}
