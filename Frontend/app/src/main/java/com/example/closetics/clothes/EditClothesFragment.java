package com.example.closetics.clothes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.MainActivity;

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Calls the base fragment onCreateView
        super.onViewCreated(view, savedInstanceState);
        String field = getClothingItemField(getPosition());




        //Textfields
        if (getPosition()>1 && getPosition()<9) {
            if (!field.equals("null")) {
                getInputField().setText(field);
            }
            //image
        }
        else if(getPosition() == 0){
            if (clothingItem.getImage() != null){
                setImageView();
            }
        }
        //Spinners
        else if (getPosition() == 1 ){
            Spinner spinner = getSpinner();
            Log.d("string", clothingItem.getFavorite());
            String bool = clothingItem.getFavorite();
            if (bool.equals("false")){
                spinner.setSelection(1);
            }
            else{
                spinner.setSelection(0);
            }
        }
        else if (getPosition() == 9){
            Integer typeI = Math.toIntExact(clothingItem.getType());
            ArrayAdapter<String> spinnerAdapter = getSpinnerAdapter();
            Spinner spinner = getSpinner();
            String type = MainActivity.CLOTHING_TYPES.get(typeI);
            String capital = type.toUpperCase();
            Character capitalLetter = capital.charAt(0);
            type = type.replace(type.charAt(0), capitalLetter);
            int spinnerPosition = spinnerAdapter.getPosition(type);
            spinner.setSelection(spinnerPosition);
        }
        else{
            Integer specialTypeI = Math.toIntExact(clothingItem.getSpecialType());
            Integer typeI = Math.toIntExact(clothingItem.getType());
            ArrayAdapter<String> spinnerAdapter = getSpinnerAdapter();
            Spinner spinner = getSpinner();
            String specialType = MainActivity.CLOTHING_SPECIAL_TYPES.get(specialTypeI);
            int spinnerPosition = spinnerAdapter.getPosition(specialType);
            spinner.setSelection(spinnerPosition);

        }



    }


    public static ClothesCreationBaseFragment newInstance(int position, ClothesDataViewModel clothesDataViewModel, ClothingItem clothingItem, CustomSlideAdapter pagerAdapter) {
        Bundle args = new Bundle();
        args.putInt("count", position);
        EditClothesFragment fragment = new EditClothesFragment(clothesDataViewModel, clothingItem, pagerAdapter);
        fragment.setArguments(args);
        return fragment;
    }
    private void setImageView(){
        Bitmap bitmap = ClothesByTypeAdapter.resizeWithAspectRatio(clothingItem.getImage(),150, 150);
        getImageView().setImageBitmap(bitmap);
    }
    private String getClothingItemField(int position){
        String field;
        if(position ==2){
            field = clothingItem.getSize();
        }
        else if (position ==3){
            field = clothingItem.getColor();
        }
        else if (position ==4){
            field = clothingItem.getDateBought();
        }
        else if (position ==5){
            field = clothingItem.getPrice();
        }
        else if (position ==6){
            field = clothingItem.getItemName();
        }
        else if (position == 7){
            field = clothingItem.getBrand();
        }
        else {
           field =  clothingItem.getMaterial();
        }
        return field;
    }


}
