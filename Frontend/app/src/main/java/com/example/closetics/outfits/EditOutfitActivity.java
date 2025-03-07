package com.example.closetics.outfits;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.closetics.MainActivity;
import com.example.closetics.R;

import java.util.ArrayList;

public class EditOutfitActivity extends AppCompatActivity {
    private final String GET_OUTFITS_URL = MainActivity.SERVER_URL + "/outfits/"; // + {{userId}}

    private TextView outfitName;
    private Button addClothingButton;
    private Button deleteButton;
    private Button doneButton;
    private ListView clothesList;
    private EditOutfitClothingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outfit);

        outfitName = findViewById(R.id.edit_outfit_outfit_name_text);
        addClothingButton = findViewById(R.id.edit_outfit_add_clothing_button);
        deleteButton = findViewById(R.id.edit_outfit_delete_button);
        doneButton = findViewById(R.id.edit_outfit_done_button);
        clothesList = findViewById(R.id.edit_outfit_clothes_list);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new EditOutfitClothingListAdapter(this, new ArrayList<>());
        clothesList.setAdapter(adapter);
    }
}