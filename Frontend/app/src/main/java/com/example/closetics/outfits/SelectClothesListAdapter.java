package com.example.closetics.outfits;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.clothes.ClothesManager;

import org.json.JSONObject;

import java.util.List;

public class SelectClothesListAdapter extends ArrayAdapter<SelectClothesListItem> {

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
        ImageView image = convertView.findViewById(R.id.select_clothes_list_item_image);

        // Populate the data into the template view using the data object
        nameText.setText(item.getName());
        colorText.setText("Color: " + item.getColor());
        typeText.setText("Type: " + item.getType() + ",");
        specialTypeText.setText(item.getSpecialType());
        includedCheck.setChecked(item.isInitiallyChecked());

        // set image
        setClothingImage(image, item);

        includedCheck.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // add item to outfit
                            OutfitManager.addClothingRequest(item.getContext(), item.getOutfitId(), item.getId(),
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
                            OutfitManager.removeClothingRequest(item.getContext(), item.getOutfitId(), item.getId(),
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

        // Return the completed view to render on screen
        return convertView;
    }

    // make list items not clickable
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private void setClothingImage(ImageView image, SelectClothesListItem item) {
        ClothesManager.getClothingImage(item.getContext(), item.getId(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.d("Volley Response", "Successfully get image of clothing " + item.getId());

                        // Display the image in the ImageView
                        if (response != null) {
                            image.setImageBitmap(response);
                        } else {
                            // display default image
                            image.setImageResource(R.drawable.clothing_mock_img); // TODO: set actual default img
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting image of clothing: " + item.getId() + ": " + error.toString());

                        // display default image
                        image.setImageResource(R.drawable.clothing_mock_img); // TODO: set actual default img
                    }
                });
    }
}
