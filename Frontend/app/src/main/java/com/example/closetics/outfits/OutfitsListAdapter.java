package com.example.closetics.outfits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.closetics.R;

import java.util.List;

public class OutfitsListAdapter  extends ArrayAdapter<OutfitsListItem> {

    public OutfitsListAdapter(Context context, List<OutfitsListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        OutfitsListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_outfits, parent, false);
        }

        // Lookup views for data population
        TextView nameText = convertView.findViewById(R.id.outfit_list_item_name_text);
        TextView clothesText = convertView.findViewById(R.id.outfit_list_item_clothes_text);

        nameText.setText(item.getName());

        String clothes = "Clothes: ";
        for (int i = 0; i < item.getClothes().size() - 1; i++) { // convert an list into comma-separated string
            clothes += item.getClothingName(i) + ", ";
        }
        clothes += item.getClothingName(item.getClothes().size() - 1);
        clothesText.setText(clothes);

        // Return the completed view to render on screen
        return convertView;
    }
}