package com.example.closetics.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.MyViewHolder> {


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_recyclerview_item, parent, false);
        LeaderboardRecyclerViewAdapter.MyViewHolder myViewHolder = new LeaderboardRecyclerViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();
        //Rank
        holder.rank.setText(String.valueOf(realPosition));
        //username
        //holder.username.setText();
        //category value




        holder.viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("functionality check", "clicked");
            }
        });




    }

    @Override
    public int getItemCount() {
        /*
        Currently set to 10 in the backend
         */
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Button viewProfileButton;
        private TextView username;
        private TextView rank;
        private TextView categoryValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewProfileButton = itemView.findViewById(R.id.view_profile);
            username = itemView.findViewById(R.id.username_value);
            rank = itemView.findViewById(R.id.rank_value);
            categoryValue = itemView.findViewById(R.id.category_value);

        }
    }
    //Inner interface
    public interface OnItemClickListener{
        //Send the view to differentiate between which button view is being pressed
        void onItemClick(int position, View view, String jsonObject);
    }


}


