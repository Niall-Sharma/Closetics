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

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.closetics.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        super.onCreate(savedInstanceState);
        ArrayList<String> objects = getArguments().getStringArrayList("JSONObject");
        long [] clothingIds = getArguments().getLongArray("clothingIds");

        adapter = new ClothesByTypeAdapter(objects, new ClothesByTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, String jsonObject) {
                //Delete button on click logic very basic for roundtrip
                 if (view.getId() == R.id.delete_button){
                     long clothingId = clothingIds[position];
                     Log.d("clothingId", String.valueOf(clothingId));
                     deleteClothing(getActivity(), clothingId, ClothesActivity.URL);
                     deleteItem(objects, position);


                 }
                 //Edit button on click logic
                 else{
                     long clothingId = clothingIds[position];
                     //updateClothing(getActivity(), clothingId, null, ClothesActivity.URL);

                 }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }


    public static Fragment newInstance(ArrayList<String> object, long[] clothingIds) {
        //Create a new forgot password fragment
        Fragment fragment = new ViewClothesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("JSONObject", object);
        args.putLongArray("clothingIds", clothingIds);
        fragment.setArguments(args);
        return fragment;
    }

    private void deleteClothing(Context context,long clothingId, String URL){
        ClothesManager.deleteClothingRequest(context, clothingId, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Response is null
                Log.d("Delete Volley Response", "Success");





            }}, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Deletion Error: " + error.toString());
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
    private void deleteItem(ArrayList<String> objects, int position){
        objects.remove(position);
        adapter.notifyItemRemoved(position);
    }

}

