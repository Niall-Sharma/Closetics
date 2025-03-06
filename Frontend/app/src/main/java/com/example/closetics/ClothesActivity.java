package com.example.closetics;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ClothesActivity extends AppCompatActivity {

    private Button addClothes;
    private Button editClothes;
    private Button viewClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        addClothes = findViewById(R.id.add_clothes);
        editClothes = findViewById(R.id.edit_clothes);
        viewClothes = findViewById(R.id.view_clothes);

        //Set these invisible for now
        //addClothes.setVisibility(View.GONE);
        //editClothes.setVisibility(View.GONE);
        viewClothes.setVisibility(View.GONE);


        //This is not pulling up the
        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddClothesFragment();
                showFragment(fragment);
            }
        });


    }


    private void showFragment(Fragment fragment) {
        Fragment existingFrag= getSupportFragmentManager().findFragmentByTag("add_clothes_fragment");
        if (existingFrag == null) {
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //New instance of forgotpassword fragment containing the id instance variables
            getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.fragmentContainerView, fragment, "add_clothes_fragment");
            //transaction.commit();
                //.replace(R.id.fragmentContainerView, fragment, "add_clothes_fragment");

            Log.d("Fragment debug", String.valueOf(fragment.isAdded()));
        }else{
            Log.d("something bad", "exists");
        }
    }


}



