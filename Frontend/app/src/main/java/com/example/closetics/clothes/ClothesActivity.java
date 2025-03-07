package com.example.closetics.clothes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.closetics.R;
import com.example.closetics.clothes.ClothesCreationBaseFragment;

import java.util.ArrayList;

public class ClothesActivity extends AppCompatActivity {

    private Button addClothes;
    private Button editClothes;
    private Button viewClothes;
    public static ArrayList<String> inputArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        addClothes = findViewById(R.id.add_clothes);
        editClothes = findViewById(R.id.edit_clothes);
        viewClothes = findViewById(R.id.view_clothes);

        //Set these invisible for now

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityItemsInvisible();
                showFragment();
            }

        });

    }

    private void showFragment(){
        ClothesCreationBaseFragment fragment = ClothesCreationBaseFragment.newInstance(ClothesCreationBaseFragment.createClothesQuestions[0], 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.add_clothes_fragment_container, fragment);
        transaction.commit();
        Log.d("Check", String.valueOf(fragment.isAdded()));

    }
    //Add more items if more added to activity
    private void activityItemsInvisible(){
        addClothes.setVisibility(View.GONE);
        editClothes.setVisibility(View.GONE);
        viewClothes.setVisibility(View.GONE);
    }







    }



















