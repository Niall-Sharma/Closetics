package com.example.closetics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class ChangePasswordFragment extends Fragment {

    private Button cancel;
    private Button submit;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private String password;
    private String confirmPassword;
    //Postman Mock server URL
    private final String URL = "https://baacab8f-1ecd-41d2-b30f-cc9889421d1d.mock.pstmn.io/updatePassword";



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
        //This request is incomplete/not sure what final backend functionality will be!!
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Post Request for change password
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();
                //String brought over from other fragment

                //Remember to make it so that there must have been security input!!
                String securityInput = null;
                try {
                    securityInput = getArguments().getString("security_input");
                }catch(NullPointerException e){
                    Log.d("Exception", "Null pointer on security input, nothing inputted");
                }

                //If the passwords are not equal
                if (!(password.equals(confirmPassword))){
                    //Prompt the user to try again
                    //
                    //
                    return;
                }


                //getContext() for fragments
                UserManager.changePasswordRequest(getContext(), confirmPassword, securityInput, URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Password change successful display message to user

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Implement proper error responses!!
                                if (error.networkResponse != null){


                                    int statusCode = error.networkResponse.statusCode;
                                    String errorBody = null;

                                    //HttpStatus.UNAUTHORIZED
                                    if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                                        try{
                                            errorBody = new String (error.networkResponse.data, "UTF-8");
                                            Log.d("Error", "401 Unauthorized");

                                        }catch(UnsupportedEncodingException e){
                                            Log.e("JSON Error", e.toString());
                                            return;


                                        }

                                    }
                                    //More

                                }
                            }
                        });


            }
        });

        return view;
    }







}
