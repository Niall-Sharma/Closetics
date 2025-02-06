package com.example.androidexample;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;





public class CounterActivity extends AppCompatActivity {

    private TextView numberTxt;
    private Button oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn;
    private Button sixBtn, sevenBtn, eightBtn, nineBtn, zeroBtn;
    private Button addBtn, subtractBtn, multiplyBtn, divideBtn, equalsBtn, clearBtn;
    private Button backButton;

    private StringBuilder currentInput = new StringBuilder();
    private int firstOperand = 0;
    private int secondOperand = 0;
    private char currentOperator = ' ';
    private boolean operationPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

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
        subtractBtn = findViewById(R.id.subtractionBtn);
        multiplyBtn = findViewById(R.id.multiplicationBtn);
        divideBtn = findViewById(R.id.divisionBtn);
        equalsBtn = findViewById(R.id.equal_btn);
        clearBtn = findViewById(R.id.clearBtn);
        backButton = findViewById(R.id.counter_back_btn);

        // Set up number button click listeners
        setNumberClickListener(oneBtn, '1');
        setNumberClickListener(twoBtn, '2');
        setNumberClickListener(threeBtn, '3');
        setNumberClickListener(fourBtn, '4');
        setNumberClickListener(fiveBtn, '5');
        setNumberClickListener(sixBtn, '6');
        setNumberClickListener(sevenBtn, '7');
        setNumberClickListener(eightBtn, '8');
        setNumberClickListener(nineBtn, '9');
        setNumberClickListener(zeroBtn, '0');

        // Set up operator click listeners
        setOperatorClickListener(addBtn, '+');
        setOperatorClickListener(subtractBtn, '-');
        setOperatorClickListener(multiplyBtn, '*');
        setOperatorClickListener(divideBtn, '/');

        equalsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                evaluateExpression();
            }

        });

        clearBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               clearCalculator();
           }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                intent.putExtra("NUM", numberTxt.getText());
                startActivity(intent);

            }
        });




    }
    private void setNumberClickListener(Button button, char number) {
        button.setOnClickListener(v -> {
            currentInput.append(number);
            numberTxt.setText(currentInput.toString());
        });
    }

    private void setOperatorClickListener(Button button, char operator) {
        button.setOnClickListener(v -> {
            if (currentInput.length() > 0) {
                if (!operationPending) {
                    firstOperand = convertStringToInt(currentInput.toString());
                    currentOperator = operator;
                    operationPending = true;
                    currentInput.setLength(0); // Clear input for next operand
                }
                numberTxt.setText(String.valueOf(firstOperand) + " " + currentOperator);
            }
        });
    }

    private void evaluateExpression() {
        if (currentInput.length() > 0 && operationPending) {
            secondOperand = convertStringToInt(currentInput.toString());

            int result = 0;
            if (currentOperator == '+') {
                result = firstOperand + secondOperand;
            } else if (currentOperator == '-') {
                result = firstOperand - secondOperand;
            } else if (currentOperator == '*') {
                result = firstOperand * secondOperand;
            } else if (currentOperator == '/') {
                if (secondOperand != 0) {
                    result = firstOperand / secondOperand;
                } else {
                    numberTxt.setText("Error");
                    return;
                }
            }

            numberTxt.setText(String.valueOf(result));
            firstOperand = result;
            currentInput.setLength(0);
            operationPending = false;
        }
    }

    private int convertStringToInt(String input) {
        int value = 0;
        for (int i = 0; i < input.length(); i++) {
            value = value * 10 + (input.charAt(i) - '0');
        }
        return value;
    }

    private void clearCalculator() {
        currentInput.setLength(0);
        firstOperand = 0;
        secondOperand = 0;
        currentOperator = ' ';
        operationPending = false;
        numberTxt.setText("");
    }
}


