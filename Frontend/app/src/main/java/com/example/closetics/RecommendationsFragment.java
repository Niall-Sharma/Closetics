package com.example.closetics;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.closetics.recommendations.RecOutfitsListAdapter;
import com.example.closetics.recommendations.RecOutfitsListItem;
import com.example.closetics.recommendations.RecUsersListAdapter;
import com.example.closetics.recommendations.RecUsersListItem;

import java.util.ArrayList;
import java.util.Arrays;

public class RecommendationsFragment extends Fragment {

    private SearchView search;
    private ImageButton backButton;
    private RecyclerView outfitsRecycler, usersRecycler;

    private RecOutfitsListAdapter outfitsAdapter;
    private RecUsersListAdapter usersAdapter;

    private boolean isStateOutfits;

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);

        // TODO: redirect to login when not logged in

        // initialize UI elements
        search = view.findViewById(R.id.rec_search);
        backButton = view.findViewById(R.id.rec_back_button);
        outfitsRecycler = view.findViewById(R.id.rec_outfits_recycler);
        usersRecycler = view.findViewById(R.id.rec_users_recycler);

        isStateOutfits = true;
        updateState();

        // initialize recycler views
        outfitsAdapter = new RecOutfitsListAdapter(new ArrayList<RecOutfitsListItem>());
        RecyclerView.LayoutManager outfitsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        outfitsRecycler.setLayoutManager(outfitsLayoutManager);
        outfitsRecycler.setItemAnimator(new DefaultItemAnimator());
        outfitsRecycler.setAdapter(outfitsAdapter);

        usersAdapter = new RecUsersListAdapter(new ArrayList<RecUsersListItem>(), item -> {
            // TODO: open public profile page by username
            Toast.makeText(getContext(), "Clicked: " + item.getUsername(), Toast.LENGTH_SHORT).show();
        });
        RecyclerView.LayoutManager usersLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        usersRecycler.setLayoutManager(usersLayoutManager);
        usersRecycler.setItemAnimator(new DefaultItemAnimator());
        usersRecycler.setAdapter(usersAdapter);

        // onClick logic
        search.setOnSearchClickListener(v -> {
            if (isStateOutfits) {
                isStateOutfits = false;
                updateState();
            }
        });

        backButton.setOnClickListener(v -> {
            isStateOutfits = true;
            search.setQuery("", false); // clear search
            search.setIconified(true);
            updateState();
        });

        populateRecommendations();

        return view;
    }

    private void populateRecommendations() {
        RecOutfitsListItem mockOutfit = new RecOutfitsListItem(1, "My old shoes", "bob002", Arrays.asList(1, 2, 3), "Very expensive", "February 3, 1976", false);
        for (int i = 0; i < 10; i++) {
            outfitsAdapter.addItem(mockOutfit);
        }
    }

    private void updateState() {
        if (isStateOutfits) {
            backButton.setVisibility(TextView.GONE);
            usersRecycler.setVisibility(TextView.GONE);
            outfitsRecycler.setVisibility(TextView.VISIBLE);
        } else {
            backButton.setVisibility(TextView.VISIBLE);
            usersRecycler.setVisibility(TextView.VISIBLE);
            outfitsRecycler.setVisibility(TextView.GONE);
        }
    }
}