package com.example.closetics.clothes;

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
    TypeGridRecyclerViewAdapter.OnItemClickListener clickListener;

    public ClothesByTypeAdapter(String[] objects, TypeGridRecyclerViewAdapter.OnItemClickListener clickListener) {
        String [] testArray = {"1"};
        this.objects = objects;
        /*
        Note: Remove later
         */
        if (objects == null){
            this.objects = testArray;
        }
    }


    @NonNull
    @Override
    public ClothesByTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_clothing_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesByTypeAdapter.MyViewHolder holder, int position) {
        holder.object.setText(objects[position]);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return objects.length;
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





}
