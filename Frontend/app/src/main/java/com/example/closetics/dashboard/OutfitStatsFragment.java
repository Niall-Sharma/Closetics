package com.example.closetics.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothesCreationBaseFragment;
import com.example.closetics.clothes.ClothesDataViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class OutfitStatsFragment extends Fragment {
    /*
    This can likely be a child of the other statistics fragment!
     */

    ArrayList<ClothingStatItem> objects;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    StatisticsRecyclerViewAdapter adapter;
    private Button back;

    /*
    Recycler view fields
     */

    public OutfitStatsFragment(ArrayList<ClothingStatItem> objects){
        this.objects = objects;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_clothes, container, false);
        super.onCreate(savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StatisticsRecyclerViewAdapter(objects ,0, getActivity());
        recyclerView.setAdapter(adapter);
        back = view.findViewById(R.id.button2);
        back.setVisibility(View.GONE);





        return view;
    }






}
