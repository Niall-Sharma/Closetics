package com.example.closetics;

import android.content.Context;
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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ForgotPasswordFragment extends Fragment {

    private Spinner spinner;
    private EditText securityInput;
    private String chosenQuestion;
    private Button submitButton;
    private Button cancelButton;
    private EditText newPassword;
    private EditText confirmPassword;
    private long index1;
    private long index2;
    private TextView errorText;
    private long userId;

    //private USERNAME


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
        submitButton = view.findViewById(R.id.change_submit_button);
        cancelButton = view.findViewById(R.id.change_cancel_button);
        newPassword = view.findViewById(R.id.change_password_edit);
        confirmPassword = view.findViewById(R.id.change_password_confirm_edit);
        errorText = view.findViewById(R.id.textView6);

        errorText.setVisibility(TextView.GONE);
        // Retrieve data from arguments
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Long id1 = getArguments().getLong("ID1");
            Long id2 = getArguments().getLong("ID2");
            userId = getArguments().getLong("userId");
            //type cast to int for indexing with arraylist
            index1 = id1.intValue();
            index2 = id2.intValue();
        }




        //Placeholders for spinner array
        //Will come from backend eventually
        ArrayList<String> allSecurityQuestions = UserManager.getSecurityQuestions();

        String[] spinnerItems = new String[]
                {allSecurityQuestions.get((int)index1), allSecurityQuestions.get((int)index2)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            //Store chosen question
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenQuestion = (String) parent.getItemAtPosition(position);
                Log.d("chosen" , chosenQuestion);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenQuestion= (String)parent.getItemAtPosition(0);
                Log.d("chosen", chosenQuestion);
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
                //Find the header of security answer
                long securityKey;
                if (chosenQuestion.equals(allSecurityQuestions.get((int)index1))){
                    securityKey = index1;
                }
                else{
                    securityKey = index2;
                }
                String securityAnswer = securityInput.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();
                String conPass = confirmPassword.getText().toString().trim();
                //Check passwords

                if (!newPass.equals(conPass)){
                    setErrorMessage("Passwords do not match");
                    return;
                }

                if (newPass.isEmpty()){
                    setErrorMessage("Please fill out the password fields");
                    return;
                }

                //Check if security answer is empty
                if (securityAnswer.isEmpty()){
                    setErrorMessage("Please answer the security question");
                    return;
                }
                updatePassword(getActivity().getApplicationContext(),userId, securityKey, securityAnswer, newPass);

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

    private void setErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
    }

    private void updatePassword(Context context, long userId, long securityKey, String securityAnswer, String newPass){
        UserManager.updatePasswordRequest(context, userId, securityKey, securityAnswer, newPass,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Only possible error on a response
                            String message = response.get("message").toString();
                            if (message.equals("Invalid request")){
                                setErrorMessage(message);
                                //Print out the response
                                Log.d("Error", message);
                            }
                            else{
                                //Password change successful, change screen
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Log.d("JSON Error", e.toString());
                        }
                    }
                },
        new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //Boilerplate
                Log.e("Volley Error", error.toString());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    setErrorMessage("Connection timeout");
                    return;
                }
                else if (error.networkResponse == null) {
                    setErrorMessage("Unknown error");
                    return;
                }
                int code = error.networkResponse.statusCode;

                if (code == HttpURLConnection.HTTP_UNAUTHORIZED){
                    //grab error body
                    String errorBody = "";
                    try{
                        errorBody = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error", e.toString());
                    }
                    if (errorBody.toLowerCase().contains("criteria")){
                        setErrorMessage("Invalid password\n" +
                                "Password must be at least 8 characters long and\n" +
                                "contain a lower case letter, an upper case letter,\n" +
                                "a digit, a special symbol (@#$%^&+=), no spaces");
                    }
                    else if(errorBody.toLowerCase().contains("old password")){
                        setErrorMessage(errorBody);
                    }
                    else{
                        setErrorMessage(errorBody);
                    }

                }
                //Account for any other un forseen  error
                else {
                    Log.e("Volley Error", "Error: " + code);
                    setErrorMessage("Error code: " + code);
                }
            }
        });
    }





    //Used in login activity to create a new forgotPassword fragment with proper arguments
    //(this is the common practice way of sending data to fragments)
    public static ForgotPasswordFragment newInstance(long id1, long id2, long userId) {
        //Create a new forgot password fragment
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        String debugid1 = String.valueOf(id1);
        String debugid2 = String.valueOf(id2);
        args.putLong("ID1", id1);
        args.putLong("ID2", id2);
        args.putLong("userId", userId);
        //Log.d("ID1", debugid1);
        //Log.d("ID2", debugid2);
        fragment.setArguments(args);
        return fragment;
    }


}

