package com.example.closetics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChangePasswordFragment extends Fragment {

    private Button cancel;
    private Button submit;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private String password;
    private String confirmPassword;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_change_password, container, false);

    //Initialize XML views
    cancel = view.findViewById(R.id.change_cancel_button);
    submit = view.findViewById(R.id.change_submit_button);
    passwordEditText = view.findViewById(R.id.change_password_edit);
    confirmPasswordEditText = view.findViewById(R.id.change_password_confirm_edit);


    //Remove this fragment, Note: add functionality
        /*
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Grabs the parent fragment manager, from the login activity
            FragmentManager fragmentManager = getParentFragmentManager();
            //Uses the tag from the login activity
            Fragment fragment = fragmentManager.findFragmentByTag("change_password_fragment");
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
                }
            }
        });


         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                

            }
        });











        return view;
    }







}
