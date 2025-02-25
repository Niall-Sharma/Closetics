package com.example.closetics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;// define signup button variable
    private Button editButton;

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

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras == null) {
            usernameText.setText("Guest (not logged in)");
        } else {
            usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
        }

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignupActivity.class);
            startActivity(intent);
        });
        //Edit user name
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


        return view;
    }
}