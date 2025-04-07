package com.example.closetics.follow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.UserManager;

import org.json.JSONObject;

import java.util.List;

public class FollowUsersListAdapter extends RecyclerView.Adapter<FollowUsersListAdapter.ViewHolder> {
    private final List<FollowUsersListItem> items;
    private final FollowUsersListAdapter.OnItemClickListener listener;

    public FollowUsersListAdapter(List<FollowUsersListItem> items, FollowUsersListAdapter.OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FollowUsersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_follow_users, parent, false);
        return new FollowUsersListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUsersListAdapter.ViewHolder holder, int position) {
        FollowUsersListItem item = items.get(position);

        String username = item.getUsername();

        holder.usernameText.setText(username);
        holder.followButton.setText(item.isFollowing() ? "Following" : "Follow");

        holder.followButton.setOnClickListener(v -> {
            if (item.isFollowing()) {
                unfollow(item.getContext(), item.getId());
                holder.followButton.setText("Follow");
            } else {
                follow(item.getContext(), item.getId());
                holder.followButton.setText("Following");
            }
            item.setFollowing(!item.isFollowing());
        });

        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FollowUsersListItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void setNewItems(List<FollowUsersListItem> newItems) {
        int oldSize = items.size();
        items.clear();
        items.addAll(newItems);
        notifyItemRangeChanged(0, Math.max(oldSize, newItems.size()));
    }

    public void clearItems() {
        int oldSize = items.size();
        items.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    private void follow(Context context, long followingId) {
        UserManager.addFollowingRequest(context, UserManager.getUserID(context), followingId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful followed " + followingId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error following " + followingId + ", Error: " + error.toString());
                    }
                });
    }

    private void unfollow(Context context, long followingId) {
        UserManager.removeFollowingRequest(context, UserManager.getUserID(context), followingId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful unfollowed " + followingId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error unfollowing " + followingId + ", Error: " + error.toString());
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameText;
        private Button followButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.follow_users_list_item_username_text);
            followButton = itemView.findViewById(R.id.follow_users_list_item_follow_button);
        }

        public void bind(final FollowUsersListItem item, final FollowUsersListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FollowUsersListItem item);
    }
}
