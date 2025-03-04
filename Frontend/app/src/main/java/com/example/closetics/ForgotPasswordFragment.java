package com.example.closetics;

import android.content.Intent;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class ForgotPasswordFragment extends Fragment {

    private Spinner spinner;
    private EditText securityInput;
    private String chosenQuestion;
    private Button submitButton;
    private Button cancelButton;
    private EditText newPassword;
    private EditText confirmPassword;
    private int index1;
    private int index2;

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
            Long id1 = getArguments().getLong("ID1");
            Long id2 = getArguments().getLong("ID2");
            //type cast to int
            index1 = id1.intValue();
            index2 = id2.intValue();
        }




        //Placeholders for spinner array
        //Will come from backend eventually
        ArrayList<String> allSecurityQuestions = UserManager.getSecurityQuestions();

        String[] spinnerItems = new String[]
                {"Select One", allSecurityQuestions.get(index1), allSecurityQuestions.get(index2)};
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
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



    //Used in login activity to create a new forgotPassword fragment with proper arguments
    public static ForgotPasswordFragment newInstance(long id1, long id2) {
        //Create a new forgot password fragment
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        String debugid1 = String.valueOf(id1);
        String debugid2 = String.valueOf(id2);
        args.putLong("ID1", id1);
        args.putLong("ID2", id2);
        Log.d("ID1", debugid1);
        Log.d("ID2", debugid2);
        fragment.setArguments(args);
        return fragment;
    }


}

