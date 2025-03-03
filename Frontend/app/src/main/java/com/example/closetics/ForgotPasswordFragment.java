package com.example.closetics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ForgotPasswordFragment extends Fragment {

    private Spinner spinner;
    private EditText securityInput;
    private String chosenQuestion;
    private Button submitButton;
    private Button cancelButton;
    private EditText newPassword;
    private EditText confirmPassword;
    private int id1;
    private int id2;

    //private USERNAME


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
                // link to Login activity XML

        securityInput = view.findViewById(R.id.security_input_password);
        spinner = view.findViewById(R.id.security_3_question_spinner);
        submitButton = view.findViewById(R.id.change_submit_button);
        cancelButton = view.findViewById(R.id.change_cancel_button);
        newPassword = view.findViewById(R.id.change_password_edit);
        confirmPassword = view.findViewById(R.id.change_password_confirm_edit);

        // Retrieve data from arguments
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id1 = getArguments().getInt("ID1");
            id2 = getArguments().getInt("ID2");
        }




        //Placeholders for spinner array
        //Will come from backend eventually
        ArrayList<String> allSecurityQuestions = UserManager.getSecurityQuestions();

        String[] spinnerItems = new String[]
                {"Select One", "Security Question 1", "Security Question 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenQuestion = (String) parent.getItemAtPosition(position);
                if (chosenQuestion.equals(spinnerItems[0])) {
                    //Item selected logic
                    //setText("Leave this field empty");
                    //else etRequest.setText("Enter JSON object here");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenQuestion= spinnerItems[0];
            }
        });


        //Remove the fragment on click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Grabs the parent fragment manager, from the login activity
                FragmentManager fragmentManager = getParentFragmentManager();
                //Uses the tag from the login activity
                Fragment fragment = fragmentManager.findFragmentByTag("forgot_password_fragment");
                if (fragment != null) {
                    fragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the securityInput data to the changePassword fragment
                String securityInputString = securityInput.getText().toString();
                Log.d("tag", securityInputString);
                Bundle bundle = new Bundle();
                bundle.putString("security_input", securityInputString);
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                fragment.setArguments(bundle);
                //Show the fragment
                showFragment(fragment);
            }
        });


        return view;
    }

    private void showFragment(Fragment fragment){
        //Changed to getParentFragmentManager
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        //Fragment fragment = new ChangePasswordFragment();
        transaction.replace(R.id.forgot_password_fragment_container, fragment, "change_password_fragment");
        transaction.commit();

    }


    //Accepting the data from loginActivity
    public static ForgotPasswordFragment newInstance(int data1, int data2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putInt("ID1", data1);
        args.putInt("ID2", data2);
        fragment.setArguments(args);
        return fragment;
    }


}

