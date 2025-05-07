package com.example.closetics;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.closetics.clothes.CameraActivity;
import com.example.closetics.clothes.ClothesActivity;
import com.example.closetics.outfits.OutfitsActivity;

public class ClothesFragment extends Fragment {

    private Button clothesButton;
    private Button outfitsButton;
    private TextView loginText;

    public ClothesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clothes, container, false);

        // redirect to profile if not logged in
        //((MainActivity)getActivity()).redirectToFragment(3);

        clothesButton = view.findViewById(R.id.discover_clothes_button);
        outfitsButton = view.findViewById(R.id.discover_outfits_button);
        loginText = view.findViewById(R.id.discover_login_text);

        // show/hide elements depending if user is logged in
        String username = UserManager.getUsername(getActivity().getApplicationContext());
        if(username == null) {
            loginText.setVisibility(TextView.VISIBLE);
            clothesButton.setVisibility(TextView.GONE);
            outfitsButton.setVisibility(TextView.GONE);
        } else {
            loginText.setVisibility(TextView.GONE);
            clothesButton.setVisibility(TextView.VISIBLE);
            outfitsButton.setVisibility(TextView.VISIBLE);
        }

        clothesButton.setOnClickListener(v -> {
            ClothesActivity.getUserClothing(getActivity(), UserManager.getUserID(getActivity()), ClothesActivity.URL);

        });

        outfitsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OutfitsActivity.class);
            startActivity(intent);
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // redirect to profile if not logged in
        if (UserManager.getUsername(getActivity().getApplicationContext()) == null) {
            ((MainActivity)getActivity()).redirectToFragment(3);
        }
    }

}