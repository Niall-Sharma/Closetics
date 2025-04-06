package com.example.closetics.follow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.recommendations.RecUsersListAdapter;
import com.example.closetics.recommendations.RecUsersListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowingTabFragment extends Fragment {

    private TextView emptyText;
    private RecyclerView recycler;
    private FollowUsersListAdapter adapter;


    private long userId;

    public FollowingTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following_tab, container, false);

        // get userId whose lists we are displaying
        Bundle extras = getActivity().getIntent().getExtras();
        userId = -1;
        if (extras != null) {
            userId = extras.getLong("USER_ID", -1);
        }

        // initialize ui
        emptyText = view.findViewById(R.id.following_tab_empty_text);
        recycler = view.findViewById(R.id.following_tab_recycler);

        // hide empty text by default
        emptyText.setVisibility(TextView.GONE);

        // initialize recycler
        adapter = new FollowUsersListAdapter(new ArrayList<FollowUsersListItem>(), item -> {
            // TODO: open public profile page by username
            Toast.makeText(getContext(), "Clicked: " + item.getUsername(), Toast.LENGTH_SHORT).show();
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        if (userId == -1) {
            Log.e("FollowingTabFragment", "User Id not passed");
        } else {
            populateFollowing(userId);
        }

        return view;
    }

    private void populateFollowing(long userId) {
        // mock values for testing
        ArrayList<FollowUsersListItem> mockListItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockListItems.add(new FollowUsersListItem(getActivity().getApplicationContext(),
                    i + 1, "mockUser" + (i + 1),
                    i % 2 == 0));
        }
        adapter.setNewItems(mockListItems);
        return;

        /*
        UserManager.getFollowingListRequest(getActivity().getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Volley Response", "Successful get following of " + userId + ": " + response.toString());

                            ArrayList<FollowUsersListItem> usersListItems = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                // TODO: adjust this to real values
                                usersListItems.add(new FollowUsersListItem(getActivity().getApplicationContext(),
                                        user.getLong("userId"), user.getString("username"),
                                        user.getBoolean("isFollowing")));
                            }

                            // update recycler only if there is anything in the list
                            if (usersListItems.isEmpty()) {
                                emptyText.setVisibility(TextView.VISIBLE);
                            } else {
                                adapter.setNewItems(usersListItems);
                            }
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get following of " + userId + " Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get following of " + userId + " Error: " + error.toString());
                    }
                });
        */
    }
}