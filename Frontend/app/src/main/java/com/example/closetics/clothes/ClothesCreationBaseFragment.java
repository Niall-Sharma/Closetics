package com.example.closetics.clothes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.R;

public class ClothesCreationBaseFragment extends Fragment{
    private static final String TAG = "MyFragment";

    private Button submit;
    private TextView clothesTextView;
    private EditText inputField;
    private ClothesDataViewModel clothesDataViewModel;
    //If true this is an edit fragment
    private boolean edit;

    private ViewPager2 viewPager;

    //Add camera functionality
    //Likely will need to add more fields to this and more fragments!!!
    public static String[] createClothesQuestions= { "Would you like to favorite this item?","Size of this item?","What color is it?","What date was it bought?",
            "What is its price?", "What would you like to call this piece of clothing?",
            "What is the brand?", "What material is it?"
    };

    public ClothesCreationBaseFragment(ClothesDataViewModel clothesDataViewModel){
        this.clothesDataViewModel = clothesDataViewModel;

    }
    /*
    Second constructor for editing viewpager
     */
    public ClothesCreationBaseFragment(ClothesDataViewModel clothesDataViewModel, String answeredQuestions ){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_clothes, container, false);
        super.onCreate(savedInstanceState);

        int index = getArguments().getInt("count");

        submit = view.findViewById(R.id.add_button);
        clothesTextView = view.findViewById(R.id.question_text);
        inputField = view.findViewById(R.id.input_edit);

        clothesTextView.setText(createClothesQuestions[index]);

        //Grab the viewpager
        viewPager = requireActivity().findViewById(R.id.edit_pager);

        inputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Update the arrayList when text is changed

                //If there is a change set it in the arrayList
                if (!(s.toString().equals(""))) {
                    clothesDataViewModel.setFragment(index, inputField.getText().toString().trim());
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            //Swipe on next button
            @Override
            public void onClick(View v) {
                if (viewPager!=null){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
                }
            }
        });


        return view;
    }



    public static ClothesCreationBaseFragment newInstance(int fragmentCount, ClothesDataViewModel clothesDataViewModel) {
        //Create a new forgot password fragment
        ClothesCreationBaseFragment fragment = new ClothesCreationBaseFragment(clothesDataViewModel);
        Bundle args = new Bundle();
        args.putInt("count", fragmentCount);
        fragment.setArguments(args);
        return fragment;
    }



}



