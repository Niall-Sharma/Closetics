package com.example.closetics.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.MyViewHolder>{
    /*
    Baseline clothes stats adapter
     */
    String [] objects;

    public StatisticsRecyclerViewAdapter(String[] objects){
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_recycler_view_item, parent, false);
        StatisticsRecyclerViewAdapter.MyViewHolder myViewHolder = new StatisticsRecyclerViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();
        holder.object.setText(objects[realPosition]);

    }

    @Override
    public int getItemCount() {
        return objects.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView object;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            object = itemView.findViewById(R.id.stat_object);

        }
    }
}
