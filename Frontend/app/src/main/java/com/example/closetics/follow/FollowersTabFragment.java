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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowersTabFragment extends Fragment {

    private TextView emptyText;
    private RecyclerView recycler;
    private FollowUsersListAdapter adapter;

    private long userId;

    public FollowersTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers_tab, container, false);

        // get userId whose lists we are displaying
        Bundle extras = getActivity().getIntent().getExtras();
        userId = -1;
        if (extras != null) {
            userId = extras.getLong("USER_ID", -1);
        }

        // initialize ui
        emptyText = view.findViewById(R.id.followers_tab_empty_text);
        recycler = view.findViewById(R.id.followers_tab_recycler);

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
            Log.e("FollowersTabFragment", "User Id not passed");
        } else {
            populateFollowers(userId);
        }

        return view;
    }

    private void populateFollowers(long userId) {
        // first get logged in user's following list
        UserManager.getFollowingListRequest(getActivity().getApplicationContext(), UserManager.getUserID(getActivity().getApplicationContext()),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Volley Response", "Successful get following of current user: " + response.toString());

                            ArrayList<Long> myFollowingIds = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                myFollowingIds.add(response.getJSONObject(i).getLong("id"));
                            }

                            // continue call with following ids on hand
                            populateFollowers(userId, myFollowingIds);
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get following of current user Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get following of current user Error: " + error.toString());
                    }
                });
    }

    private void populateFollowers(long userId, List<Long> myFollowingIds) {
        // mock values for testing
//        ArrayList<FollowUsersListItem> mockListItems = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            mockListItems.add(new FollowUsersListItem(getActivity().getApplicationContext(),
//                    i + 1, "mockUser" + (i + 1),
//                    i % 2 == 0));
//        }
//        adapter.setNewItems(mockListItems);
//        return;

        UserManager.getFollowersListRequest(getActivity().getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Volley Response", "Successful get followers of " + userId + ": " + response.toString());

                            ArrayList<FollowUsersListItem> usersListItems = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);

                                boolean isFollowing = myFollowingIds.contains(user.getLong("id"));

                                usersListItems.add(new FollowUsersListItem(getActivity().getApplicationContext(),
                                        user.getLong("id"), user.getString("username"),
                                        isFollowing));
                            }

                            // update recycler only if there is anything in the list
                            if (usersListItems.isEmpty()) {
                                emptyText.setVisibility(TextView.VISIBLE);
                            } else {
                                adapter.setNewItems(usersListItems);
                            }
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get followers of " + userId + " Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get followers of " + userId + " Error: " + error.toString());
                    }
                });
    }
}