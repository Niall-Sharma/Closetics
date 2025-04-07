package com.example.closetics;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.closetics.follow.FollowActivity;

public class ProfileFragment extends Fragment {

    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable
    private Button logoutButton;
    private Button editButton;
    private Button deleteUserButton;
    private Button followingButton, followersButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // initialize UI elements
        usernameText = view.findViewById(R.id.profile_username_text);
        loginButton = view.findViewById(R.id.profile_login_button);
        signupButton = view.findViewById(R.id.profile_signup_button);
        editButton = view.findViewById(R.id.profile_edit_user_button);
        deleteUserButton = view.findViewById(R.id.profile_delete_user_button);
        logoutButton = view.findViewById(R.id.profile_logout_button);
        followingButton = view.findViewById(R.id.profile_following_button);
        followersButton = view.findViewById(R.id.profile_followers_button);

        String username = UserManager.getUsername(getActivity().getApplicationContext());
        if(username == null) {
            usernameText.setText("Guest (not logged in)");
            loginButton.setVisibility(TextView.VISIBLE);
            signupButton.setVisibility(TextView.VISIBLE);
            logoutButton.setVisibility(TextView.GONE);
            editButton.setVisibility(TextView.GONE);
            deleteUserButton.setVisibility(TextView.GONE);
            followingButton.setVisibility(TextView.GONE);
            followersButton.setVisibility(TextView.GONE);
        } else {
            usernameText.setText(username);
            loginButton.setVisibility(TextView.GONE);
            signupButton.setVisibility(TextView.GONE);
            logoutButton.setVisibility(TextView.VISIBLE);
            editButton.setVisibility(TextView.VISIBLE);
            deleteUserButton.setVisibility(TextView.VISIBLE);
            followingButton.setVisibility(TextView.VISIBLE);
            followersButton.setVisibility(TextView.VISIBLE);
        }

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignupActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            UserManager.clearSavedData(view.getContext());

            usernameText.setText("Guest (not logged in)");
            loginButton.setVisibility(TextView.VISIBLE);
            signupButton.setVisibility(TextView.VISIBLE);
            logoutButton.setVisibility(TextView.GONE);
            editButton.setVisibility(TextView.GONE);
            deleteUserButton.setVisibility(TextView.GONE);
            followingButton.setVisibility(TextView.GONE);
            followersButton.setVisibility(TextView.GONE);
        });

        // Edit user name
        editButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //This button only has functionality when the user is logged in!
                //Safe way for fragments to get context!! (requireContext)
                String token = UserManager.getLoginToken(requireContext());
                //No functionality if token does equal null normally
                if (token != null){


                }
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                startActivity(intent);

            }
        });

        deleteUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DeleteUserActivity.class);
            startActivity(intent);
        });

        followingButton.setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_followFragment);
            Intent intent = new Intent(getActivity(), FollowActivity.class);
            intent.putExtra("IS_FOLLOWING", true);
            intent.putExtra("USER_ID", UserManager.getUserID(getActivity()));
            startActivity(intent);
        });

        followersButton.setOnClickListener(v -> {
//            Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_followFragment);
            Intent intent = new Intent(getActivity(), FollowActivity.class);
            intent.putExtra("IS_FOLLOWING", false);
            intent.putExtra("USER_ID", UserManager.getUserID(getActivity()));
            startActivity(intent);
        });

        return view;
    }
}