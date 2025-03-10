package com.example.closetics.clothes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.closetics.R;

public class ViewClothesFragment extends Fragment {
    TextView objectText;
    String object;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_clothes, container, false);

        objectText = view.findViewById(R.id.clothesTextView);
        super.onCreate(savedInstanceState);

        object = getArguments().getString("string");

        objectText.setText(object);











        return view;
    }


    public static Fragment newInstance(String get) {
        //Create a new forgot password fragment
        Fragment fragment = new ViewClothesFragment();
        Bundle args = new Bundle();
        args.putString("string", get);
        fragment.setArguments(args);
        return fragment;
    }

}
