package com.example.closetics.clothes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewClothesFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ClothesByTypeAdapter adapter;
    Button backButton;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_clothes, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        backButton = view.findViewById(R.id.button2);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        super.onCreate(savedInstanceState);
        ArrayList<ClothingItem> clothingItems = (ArrayList<ClothingItem>)getArguments().getSerializable("ClothingItems");

        adapter = new ClothesByTypeAdapter(clothingItems, new ClothesByTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, ClothingItem jsonObject) {
                //Delete button on click logic very basic for roundtrip
                int positionNeed = position;
                 if (view.getId() == R.id.delete_button){
                     long clothingId = jsonObject.getId();
                     Log.d("clothingId", String.valueOf(clothingId));
                     deleteClothing(getActivity(), clothingId, clothingItems, position, ClothesActivity.URL);

                 }
                 //Edit button on click logic
                 else{
                     long clothingId = jsonObject.getId();
                     ClothingItem clothingItem = clothingItems.get(position);
                     //Switch to editActivity
                     Intent intent = new Intent(getActivity(), EditClothesActivity.class);
                     intent.putExtra("clothingId", clothingId);
                     //Serializable
                     intent.putExtra("clothingItem", clothingItem);
                     startActivity(intent);


                 }
            }
        });

        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ClothesActivity.class);
                startActivity(intent);
            }
        });
        //recyclerView.setHasFixedSize(true);

        return view;
    }


    public static Fragment newInstance(ArrayList<String> object, ArrayList<ClothingItem> clothingItem) {
        //Create a new forgot password fragment
        Fragment fragment = new ViewClothesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("JSONObject", object);
        args.putSerializable("ClothingItems", clothingItem);
        fragment.setArguments(args);
        return fragment;
    }

    private void deleteClothingFromOutfit(Context context, Long clothingId, Long outfitId){
        OutfitManager.removeClothingRequest(context, clothingId, outfitId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("", );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void deleteClothing(Context context,long clothingId, ArrayList<ClothingItem> objects, int position, String URL){
        ClothesManager.deleteClothingRequest(context, clothingId, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Response is null
                Log.d("Delete Volley Response", "Success");
                deleteItem(objects, position);



            }}, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Deletion Error: " + error.toString());
                    }
                }

        );
    }
    private void deleteItem(ArrayList<ClothingItem> objects, int position){
        //Add a check for index out of bounds etc.
        objects.remove(position);
        adapter.notifyItemRemoved(position);
    }

}

