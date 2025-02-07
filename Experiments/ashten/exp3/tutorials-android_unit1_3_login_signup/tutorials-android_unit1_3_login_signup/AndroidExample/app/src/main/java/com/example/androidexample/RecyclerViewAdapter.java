package com.example.androidexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<ColorWidget> colorWidgetsList;

    public RecyclerViewAdapter(Context context, ArrayList<ColorWidget> colorWidgetsList){
        this.context= context;
        this.colorWidgetsList = colorWidgetsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Converting the xml layout to actual object on the screen (inflating the layout)
        //Give the look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent,false);

        return new RecyclerViewAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        //Assigning values to the views we created in the recycler_view_row.xml
        //based on the position of the recycler view

        holder.textViewColor.setText(colorWidgetsList.get(position).getColorName());
        holder.textViewTint.setText(colorWidgetsList.get(position).getTintName());
        holder.imageView.setImageResource(colorWidgetsList.get(position).getImage());



    }

    @Override
    public int getItemCount() {
        //number of items you want displayed
        return colorWidgetsList.size();
    }

    //Inner class, only used here!
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing the views from our recycler_view_row layout file
        //Similar to onCreate method

        ImageView imageView;
        TextView textViewColor, textViewTint;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewColor = itemView.findViewById(R.id.textViewColor);
            textViewTint = itemView.findViewById(R.id.textViewTint);


        }
    }
}
