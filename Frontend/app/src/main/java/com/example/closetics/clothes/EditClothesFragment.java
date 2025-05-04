package com.example.closetics.clothes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

public class EditClothesFragment extends ClothesCreationBaseFragment{

    private ClothingItem clothingItem;
    private CustomSlideAdapter pagerAdapter;


    public EditClothesFragment(ClothesDataViewModel clothesDataViewModel, ClothingItem clothingItem, CustomSlideAdapter pagerAdapter) {
        super(clothesDataViewModel, pagerAdapter);
        this.clothingItem =clothingItem;
        this.pagerAdapter = pagerAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Calls the base fragment onCreateView
        View view = super.onCreateView(inflater, container, savedInstanceState);
        String field = getClothingItemField(getPosition());
        if (!field.equals("null")) {
            getInputField().setText(field);
        }

        return view;

    }


    public static ClothesCreationBaseFragment newInstance(int position, ClothesDataViewModel clothesDataViewModel, ClothingItem clothingItem, CustomSlideAdapter pagerAdapter) {
        Bundle args = new Bundle();
        args.putInt("count", position);
        EditClothesFragment fragment = new EditClothesFragment(clothesDataViewModel, clothingItem, pagerAdapter);
        fragment.setArguments(args);
        return fragment;
    }
    private String getClothingItemField(int position){
        String field;
        if (position ==0){
            field = clothingItem.getFavorite();
        }
        else if(position ==1){
            field = clothingItem.getSize();
        }
        else if (position ==2){
            field = clothingItem.getColor();
        }
        else if (position ==3){
            field = clothingItem.getDateBought();
        }
        else if (position ==4){
            field = clothingItem.getPrice();
        }
        else if (position ==5){
            field = clothingItem.getItemName();
        }
        else if (position == 6){
            field = clothingItem.getBrand();
        }
        else {
           field =  clothingItem.getMaterial();
        }
        return field;
    }


}
