package com.example.closetics.clothes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

public class ClothesByTypeAdapter extends RecyclerView.Adapter<ClothesByTypeAdapter.MyViewHolder>{


    String[] objects;
    OnItemClickListener clickListener;

    public ClothesByTypeAdapter(String[] objects, OnItemClickListener clickListener) {
        this.objects = objects;
        Log.d("length", String.valueOf(objects.length));
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_clothing_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();
        String jsonObject = objects[realPosition];
        holder.object.setText(jsonObject);
        Log.d("JSON object binding", jsonObject);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, jsonObject);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, jsonObject);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.length;
        //return objects.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Button editButton;
        private Button deleteButton;
        private TextView object;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            object = itemView.findViewById(R.id.object_text);

        }
    }

    //Inner interface
    public interface OnItemClickListener{
        //Send the view to differentiate between which button view is being pressed
        void onItemClick(int position, View view, String jsonObject);
    }






}
