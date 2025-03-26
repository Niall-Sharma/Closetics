package com.example.closetics.clothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class EditClothesActivity extends AppCompatActivity {
    private Button finalSubmission;
    private TabLayout tabLayout;
    private ClothesDataViewModel clothesDataViewModel;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private long clothingId;
    private ClothingItem clothingItem;







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clothes);

        finalSubmission = findViewById(R.id.submit_button);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.edit_pager);

        //This needs an innner class
        pagerAdapter = new EditClothesActivity.ScreenSlidePagerAdapter(this);

        clothesDataViewModel = new ViewModelProvider(this).get(ClothesDataViewModel.class);

        //Initialize the fragments list
        clothesDataViewModel.setFragmentsSize(ClothesActivity.NUM_FRAGMENTS);

        //Set the viewpager
        viewPager.setAdapter(pagerAdapter);
        //Set tab mediator
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) ->{
            tab.setText(String.valueOf(position+1));
        } )).attach();

        //Set the clothingId and clothingItem instance variables
        Intent intent = getIntent();

        clothingId = intent.getLongExtra("clothingId", 0);
        clothingItem = (ClothingItem) intent.getSerializableExtra("clothingItem");




    }

    /*
Inner class for screen sliding
 */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(EditClothesActivity editClothesActivity) {
            super(editClothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            return EditClothesFragment.newInstance(position, clothesDataViewModel, clothingItem);
        }

        @Override
        public int getItemCount() {

            return ClothesActivity.NUM_FRAGMENTS;
        }
    }




}
