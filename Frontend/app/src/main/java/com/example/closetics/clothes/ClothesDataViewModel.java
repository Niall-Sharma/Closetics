package com.example.closetics.clothes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ClothesDataViewModel extends ViewModel {

    private ArrayList<LiveData<String>> fragments;
    
    //Initializae the arrayList with null values
    public ClothesDataViewModel(int fragmentCount){
        for (int i=0; i < fragmentCount; i++){
            fragments.add(null);
        }
    }


    public void setFragments(int index){
        fragments.get()
    }


}
