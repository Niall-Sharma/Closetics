package com.example.closetics.clothes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ClothesDataViewModel extends ViewModel {

    private ArrayList<MutableLiveData<String>> fragments = new ArrayList<>();
    private int fragmentCount;

    public ClothesDataViewModel(){

    }
    //Call this after instantiation!!!
    public void setFragmentsSize(int fragmentCount){
        //Initialize the arrayList with empty MutableLiveData
        this.fragmentCount = fragmentCount;
        for (int i=0; i < fragmentCount; i++){
            MutableLiveData<String> temp = new MutableLiveData<>();
            fragments.add(temp);
        }
    }


    //Add a fragment data at specific index
    public void setFragment(int index, String data){
        MutableLiveData<String> temp = new MutableLiveData<>();
        temp.setValue(data);
        fragments.add(index, temp);
    }

    //Get a fragments data at a specific index
    public MutableLiveData<String> getFragment(int index){
        return fragments.get(index);
    }

    public ArrayList<MutableLiveData<String>> getFragments(){
        return fragments;
    }





}
