package com.example.closetics.clothes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;

import org.json.JSONObject;

import java.util.Map;

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

        adapter = new ClothesByTypeAdapter(objects, new ClothesByTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, String jsonObject) {
                //Delete button on click logic very basic for roundtrip
                 if (view.getId() == R.id.delete_button){
                     long clothingId = ClothesActivity.clothingIdandIndex.get(position);
                     deleteClothing(getActivity().getApplicationContext(), clothingId, ClothesActivity.URL);
                     //Clothes

                 }
                 //Edit button on click logic
                 else{
                     //Basic roundtrip
                     long clothingId = ClothesActivity.clothingIdandIndex.get(position);
                     //updateClothing(getActivity().getApplicationContext(), clothingId, null, ClothesActivity.URL);

                 }
            }
        });
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

    private void deleteClothing(Context context,long clothingId, String URL){
        ClothesManager.deleteClothingRequest(context, clothingId, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Delete Volley Response", response.toString());
                //Logic for changing counts ...


            }}, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Delete Volley Error", error.toString());

                    }
                }

        );
    }
    private void updateClothing(Context context, long clothingId, Map<String, Object> updatedFields, String URL) {
        ClothesManager.updateClothingRequest(context, clothingId, updatedFields, URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Update Response", response.toString());


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Update Error", error.toString());
                    }
                }
        );
    }

}
