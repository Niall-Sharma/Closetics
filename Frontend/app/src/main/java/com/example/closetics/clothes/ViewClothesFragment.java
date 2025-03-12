package com.example.closetics.clothes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

public class ViewClothesFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ClothesByTypeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_clothes, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        super.onCreate(savedInstanceState);
        String[] objects = getArguments().getStringArray("JSONObject");

        adapter = new ClothesByTypeAdapter(objects);
        recyclerView.setAdapter(adapter);

        return view;
    }


    public static Fragment newInstance(String[] object) {
        //Create a new forgot password fragment
        Fragment fragment = new ViewClothesFragment();
        Bundle args = new Bundle();
        args.putStringArray("JSONObject", object);
        fragment.setArguments(args);
        return fragment;
    }

}
