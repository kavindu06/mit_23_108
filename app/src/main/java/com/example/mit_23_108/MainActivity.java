package com.example.mit_23_108;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String currentInput = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        // Set button listeners
        setButtonListeners();
    }

    private void setButtonListeners() {
        // Number and operator buttons
        int[] buttonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9, R.id.button_add, R.id.button_subtract,
                R.id.button_multiply, R.id.button_divide, R.id.button_dot, R.id.button_percent
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                currentInput += button.getText().toString();
                display.setText(currentInput);
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }

        // Equals button
        findViewById(R.id.button_equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        // Clear button
        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput = "";
                display.setText(currentInput);
            }
        });

        // Backspace button
        findViewById(R.id.button_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInput.length() > 0) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    display.setText(currentInput);
                }
            }
        });

        // AC button
        findViewById(R.id.button_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput = "";

                display.setText("");
            }
        });
    }
//result calculation
    private void calculateResult() {
        try {
            // Simple expression evaluation
            double result = evaluateExpression(currentInput);
            String resultString = String.valueOf(result);

            // Display the result
            display.setText(resultString);


            currentInput = resultString;
        } catch (Exception e) {
            display.setText("Error");
            currentInput = "";
        }
    }

    // recreate with arithmetic operations
    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                int num = 0;
                while (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                    i++;
                    if (i < expression.length()) {
                        c = expression.charAt(i);
                    } else {
                        break;
                    }
                }
                i--;
                numbers.push((double) num);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operations.isEmpty() && precedence(c) <= precedence(operations.peek())) {
                    double output = performOperation(numbers, operations);
                    numbers.push(output);
                }
                operations.push(c);
            }
        }

        while (!operations.isEmpty()) {
            double output = performOperation(numbers, operations);
            numbers.push(output);
        }

        return numbers.pop();
    }
//apply operation
    private double performOperation(Stack<Double> numbers, Stack<Character> operations) {
        double a = numbers.pop();
        double b = numbers.pop();
        char operation = operations.pop();

        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return b - a;
            case '*':
                return a * b;
            case '/':
                if (a == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return b / a;
        }
        return 0;
    }

    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }
}
