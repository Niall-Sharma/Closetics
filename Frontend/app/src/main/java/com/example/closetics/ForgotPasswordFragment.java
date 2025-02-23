package com.example.closetics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ForgotPasswordFragment extends Fragment {

    private TextView instructions;
    private Spinner spinner;
    private EditText securityInput;
    private String chosenQuestion;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
                // link to Login activity XML

        securityInput = view.findViewById(R.id.security_input_password);
        spinner = view.findViewById(R.id.security_3_question_spinner);
        instructions = view.findViewById(R.id.security_instructions);


        //Placeholders for spinner array
        //Will come from backend eventually
        String[] spinnerItems = new String[]
                {"Security Question 1", "Security Question 2", "Security Question 3"};
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
        Bundle extras = getActivity().getIntent().getExtras();





        return view;
    }

}

