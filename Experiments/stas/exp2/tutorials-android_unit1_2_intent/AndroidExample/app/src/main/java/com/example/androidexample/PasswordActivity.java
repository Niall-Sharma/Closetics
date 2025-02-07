package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

public class PasswordActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Chip yesChip, noChip;
    private Switch questionSwitch;
    private RadioButton goodRadioButton, pardonRadioButton;
    private ImageButton sendButton, cancelButton;
    private RadioGroup radioGroup;

    private int counter = 0;
    private boolean chip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // ??
        setContentView(R.layout.activity_password);

        // ???
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        passwordEditText = findViewById(R.id.password_edit_text);
        yesChip = findViewById(R.id.yes_chip);
        noChip = findViewById(R.id.no_chip);
        questionSwitch = findViewById(R.id.question_switch);
        goodRadioButton = findViewById(R.id.good_radio_button);
        pardonRadioButton = findViewById(R.id.pardon_radio_button);
        sendButton = findViewById(R.id.send_button);
        cancelButton = findViewById(R.id.cancel_button);
        radioGroup = findViewById(R.id.radio_group);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("NUM")) {
            counter = extras.getInt("NUM");
        }

        yesChip.setOnClickListener((View v) -> chip = true);
        noChip.setOnClickListener((View v) -> chip = false);

        cancelButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
            intent.putExtra("NUM", counter);
            startActivity(intent);
        });

        sendButton.setOnClickListener((View v) -> {
            String pwd = passwordEditText.getText().toString().trim();
            if (pwd.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
                return; // not allowed to submit empty password
            }

            Intent intent = new Intent(PasswordActivity.this, ResultActivity.class);

            intent.putExtra("NUM", counter);
            intent.putExtra("password", pwd);
            intent.putExtra("chip", chip);
            intent.putExtra("switch", questionSwitch.isChecked());
            intent.putExtra("radio", radioGroup.getCheckedRadioButtonId());
            startActivity(intent);
        });
    }
}