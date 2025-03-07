package com.example.closetics.outfits;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;

import org.json.JSONObject;

import java.util.List;

public class SelectClothesListAdapter extends ArrayAdapter<SelectClothesListItem> {
    private final String URL_REMOVE_ITEM = MainActivity.SERVER_URL + "/removeItemFromOutfit/"; // + {{outfitId}} + / + {{clothingId}}
    private final String URL_ADD_ITEM = MainActivity.SERVER_URL + "/addItemToOutfit/"; // + {{outfitId}} + / + {{clothingId}}

    public SelectClothesListAdapter(Context context, List<SelectClothesListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        SelectClothesListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_select_clothes, parent, false);
        }

        // Lookup view for data population
        TextView nameText = convertView.findViewById(R.id.select_clothes_list_item_name_text);
        TextView colorText = convertView.findViewById(R.id.select_clothes_list_item_color_text);
        TextView typeText = convertView.findViewById(R.id.select_clothes_list_item_type_text);
        TextView specialTypeText = convertView.findViewById(R.id.select_clothes_list_item_special_type_text);
        CheckBox includedCheck = convertView.findViewById(R.id.select_clothes_list_item_check);

        includedCheck.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // add item to outfit
                            OutfitManager.addClothingRequest(item.getContext(), item.getOutfitId(), item.getId(), URL_ADD_ITEM,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("Volley Response", response.toString());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Volley Error", error.toString());
                                        }
                                    });
                        } else {
                            // delete item from outfit
                            OutfitManager.removeClothingRequest(item.getContext(), item.getOutfitId(), item.getId(), URL_REMOVE_ITEM,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("Volley Response", response.toString());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Volley Error", error.toString());
                                        }
                                    });
                        }
                    }
                });

        // Populate the data into the template view using the data object
        nameText.setText(item.getName());
        colorText.setText("Color: " + item.getColor());
        typeText.setText("Type: " + item.getType() + ",");
        specialTypeText.setText(item.getSpecialType());
        includedCheck.setChecked(item.isInitiallyChecked());

        // Return the completed view to render on screen
        return convertView;
    }

    // make list items not clickable
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
