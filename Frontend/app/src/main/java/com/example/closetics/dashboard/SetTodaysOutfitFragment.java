package com.example.closetics.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.closetics.R;


public class SetTodaysOutfitFragment extends Fragment {

    public SetTodaysOutfitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_today, container, false);
        return view;
    }


    public static Fragment newInstance() {
        //Create a new forgot password fragment
        Fragment fragment = new SetTodaysOutfitFragment();
        Bundle args = new Bundle();
        //args.putStringArrayList("JSONObject", object);
        //args.putLongArray("clothingIds", clothingIds);
        //args.putSerializable("ClothingItems", clothingItem);
        fragment.setArguments(args);
        return fragment;
    }
}
