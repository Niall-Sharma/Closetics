package com.example.closetics.clothes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.closetics.ForgotPasswordFragment;
import com.example.closetics.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ClothesCreationBaseFragment extends Fragment{
    private static final String TAG = "MyFragment";

    private Button submit;
    private TextView clothesTextView;
    private EditText inputField;

    //Add camera functionality
    //Likely will need to add more fields to this and more fragments!!!
    public static String[] createClothesQuestions= {"Size of this item?", "Would you like to favorite this item?",
            "When did you last wear this item?", "How many times worn?",
            "What color is it?", "What date was it bought?",
            "What is the brand?", "What material is it?"
    };

    public ClothesCreationBaseFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_clothes, container, false);

        submit = view.findViewById(R.id.add_button);
        clothesTextView = view.findViewById(R.id.question_text);
        inputField = view.findViewById(R.id.input_edit);
        clothesTextView.setText("pooooooooooo");


        /*
        if (getArguments() != null) {
            int count = getArguments().getInt("count");
            String question = getArguments().getString("question");
            String answer = getArguments().getString("answer");
            //Set the textview with the question
            clothesTextView.setText(question);

            //If not first fragment, send answer
            if (!(count<1)){
                ClothesActivity.inputArray.add(answer);
            }


            if (count < createClothesQuestions.length - 1){
                count ++;
                submit.setText("Next Question");

                int finalCount = count;
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadNextFragment(createClothesQuestions[finalCount], answer, finalCount);
                    }
                });

            }
            else{
                submit.setText("Finish");

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Still need to save the answer
                        ClothesActivity.inputArray.add(inputField.getText().toString());

                        //Go back to activity
                        Intent intent  = new Intent(getActivity().getApplicationContext(), ClothesActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        */

        return view;
    }

    public static ClothesCreationBaseFragment newInstance(String question, int fragmentCount) {
        //Create a new forgot password fragment
        ClothesCreationBaseFragment fragment = new ClothesCreationBaseFragment();
        Bundle args = new Bundle();
        args.putInt("count", fragmentCount);
        args.putString("question", question);
        //Log.d("ID1", debugid1);
        //Log.d("ID2", debugid2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ClothesCreationBaseFragment newInstance(String question, int fragmentCount) {
        //Create a new forgot password fragment
        ClothesCreationBaseFragment fragment = new ClothesCreationBaseFragment();
        Bundle args = new Bundle();
        args.putInt("count", fragmentCount);
        args.putString("question", question);
        //Log.d("ID1", debugid1);
        //Log.d("ID2", debugid2);
        fragment.setArguments(args);
        return fragment;
    }
    private void loadNextFragment(String question, String answer, int fragmentCount){
        ClothesCreationBaseFragment fragment = newInstance(createClothesQuestions[fragmentCount], answer, fragmentCount);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.add_clothes_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    //Debugging methods
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment is visible (started)");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment is now interactive (resumed)");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment view destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment detached from activity");
    }


}



