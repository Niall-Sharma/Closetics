package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private Button oneBtn; // define increase button variable
    private Button twoBtn; // define decrease button variable
    private Button backBtn;     // define back button variable
    private Button threeBtn;
    private Button fourBtn;
    private Button fiveBtn;
    private Button sixBtn;
    private Button sevenBtn;
    private Button eightBtn;
    private Button nineBtn;
    private Button zeroBtn;
    private Button addBtn;
    private Button subtractBtn;
    private Button divideBtn;
    private Button multiplyBtn;
    private Button equalsBtn;
    private String fullExpression;
    private Boolean doubleOperation;

    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /* initialize UI elements */

        numberTxt = findViewById(R.id.number);
        oneBtn = findViewById(R.id.oneBtn);
        twoBtn = findViewById(R.id.twoBtn);
        threeBtn = findViewById(R.id.threeBtn);
        fourBtn = findViewById(R.id.fourBtn);
        fiveBtn = findViewById(R.id.fiveBtn);
        sixBtn = findViewById(R.id.sixBtn);
        sevenBtn = findViewById(R.id.sevenBtn);
        eightBtn = findViewById(R.id.eightBtn);
        nineBtn = findViewById(R.id.nineBtn);
        zeroBtn = findViewById(R.id.zeroBtn);
        addBtn = findViewById(R.id.additionBtn);
        divideBtn = findViewById(R.id.divisionBtn);
        multiplyBtn = findViewById(R.id.multiplicationBtn);
        subtractBtn = findViewById(R.id.subtractionBtn);
        backBtn = findViewById(R.id.counter_back_btn);
        equalsBtn = findViewById(R.id.equal_btn);

        //Because these are called inside of oncreate, it is okay to have the methods outside!
        //intializing the buttons with their onClick logic
        doubleOperation=false;
        numberButtonClick(oneBtn, "1");
        numberButtonClick(twoBtn,"2" );
        numberButtonClick(threeBtn,"3" );
        numberButtonClick(fourBtn,"4" );
        numberButtonClick(fiveBtn,"5" );
        numberButtonClick(sixBtn,"6" );
        numberButtonClick(sevenBtn,"7" );
        numberButtonClick(eightBtn,"8" );
        numberButtonClick(nineBtn,"9" );
        numberButtonClick(zeroBtn,"0" );

        //intitializing the operations with their onClick logic
        operationButtonClick(multiplyBtn, "*");
        operationButtonClick(subtractBtn, "-");
        operationButtonClick(addBtn, "+");
        operationButtonClick(divideBtn, "/");

        //When 

        //When equals button is pressed (evaluate the expression)
        equalsBtn.setOnClickListener(new View.OnClickListener(){;
        @Override
        public void onClick(View v){

        }

        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });
    }

    //Methods defined outside of onClick

    private void operationButtonClick (Button opButton, String operation){
        opButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (doubleOperation){
                    return;
                }
                //Save expression
                fullExpression = numberTxt.getText() + operation;
                System.out.println(fullExpression);
                //Clear widget
                numberTxt.setText("");
                doubleOperation = true;

            }

        });

    }
    //Number is true, operation is false
    private void numberButtonClick (Button anyButton, String number){
        anyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //fullExpression+=number;//Add to fullExpression
                numberTxt.append(number); //Append the number to textView
                doubleOperation=false;

            }
        });
    }

}