package com.example.mad_individual;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class OrderNum extends AppCompatActivity {

    private TextView[] numTextViews;
    private Button[] buttons;
    private Button btnUndo;
    private List<Integer> numbers;
    private List<Integer> clickedNumbers;
    private Stack<Integer> clickedButtonIndices; // To track which buttons were clicked
    private int currentPosition = 0;
    private int totalPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_num);

        // Initialize UI elements
        numTextViews = new TextView[]{
                findViewById(R.id.textView7),  // Num1
                findViewById(R.id.textView14), // Num2
                findViewById(R.id.textView15), // Num3
                findViewById(R.id.textView16), // Num4
                findViewById(R.id.textView17)  // Num5
        };

        buttons = new Button[]{
                findViewById(R.id.button),   // b1
                findViewById(R.id.button6),  // b2
                findViewById(R.id.button7),  // b3
                findViewById(R.id.button8),  // b4
                findViewById(R.id.button9)   // b5
        };

        btnUndo = findViewById(R.id.btnUndo);
        clickedButtonIndices = new Stack<>();

        // Generate unique random 2-digit numbers
        generateRandomNumbers();

        // Set click listeners for buttons
        for (int i = 0; i < buttons.length; i++) {
            final int buttonIndex = i;
            buttons[i].setOnClickListener(v -> onButtonClick(buttonIndex));
        }

        // Set click listener for Undo button
        btnUndo.setOnClickListener(v -> undoLastAction());
    }

    private void generateRandomNumbers() {
        Random random = new Random();
        HashSet<Integer> uniqueNumbers = new HashSet<>();

        // Generate 5 unique 2-digit numbers
        while (uniqueNumbers.size() < 5) {
            int num = 10 + random.nextInt(90); // 10-99
            uniqueNumbers.add(num);
        }

        numbers = new ArrayList<>(uniqueNumbers);
        clickedNumbers = new ArrayList<>();
        clickedButtonIndices.clear();
        currentPosition = 0;
        totalPoints = 0;

        // Reset all numTextViews to invisible
        for (TextView tv : numTextViews) {
            tv.setVisibility(View.INVISIBLE);
        }

        // Set the random numbers to buttons and enable them
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(String.valueOf(numbers.get(i)));
            buttons[i].setVisibility(View.VISIBLE);
            buttons[i].setEnabled(true);
        }

        // Disable undo button at game start
        btnUndo.setEnabled(false);
    }

    private void onButtonClick(int buttonIndex) {
        if (currentPosition >= numTextViews.length) return;

        // Get the clicked number
        int clickedNumber = Integer.parseInt(buttons[buttonIndex].getText().toString());
        clickedNumbers.add(clickedNumber);
        clickedButtonIndices.push(buttonIndex); // Store the button index for undo

        // Display the number in the current position
        numTextViews[currentPosition].setText(String.valueOf(clickedNumber));
        numTextViews[currentPosition].setVisibility(View.VISIBLE);

        // Disable the clicked button
        buttons[buttonIndex].setEnabled(false);

        currentPosition++;

        // Enable undo button after first click
        btnUndo.setEnabled(true);

        // Check if all buttons have been clicked
        if (currentPosition == numTextViews.length) {
            calculateScore();
            showResultDialog();
        }
    }

    private void undoLastAction() {
        if (currentPosition <= 0) return;

        // Get the last clicked button index
        int lastButtonIndex = clickedButtonIndices.pop();

        // Hide the last number display
        currentPosition--;
        numTextViews[currentPosition].setVisibility(View.INVISIBLE);

        // Re-enable the button
        buttons[lastButtonIndex].setEnabled(true);

        // Remove the last clicked number
        clickedNumbers.remove(clickedNumbers.size() - 1);

        // Disable undo button if no more actions to undo
        if (currentPosition == 0) {
            btnUndo.setEnabled(false);
        }
    }

    private void calculateScore() {
        // Create a sorted list of the numbers for comparison
        List<Integer> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);

        // Check each position for correctness
        for (int i = 0; i < clickedNumbers.size(); i++) {
            if (clickedNumbers.get(i).equals(sortedNumbers.get(i))) {
                totalPoints += 20;
            }
        }
    }

    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("Your score: " + totalPoints + " points");

        builder.setPositiveButton("Play Again", (dialog, which) -> {
            // Reset the game
            resetGame();
        });

        builder.setNegativeButton("Back", (dialog, which) -> {
            // Navigate back to main menu
            Intent intent = new Intent(OrderNum.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void resetGame() {
        // Hide all numTextViews
        for (TextView tv : numTextViews) {
            tv.setVisibility(View.INVISIBLE);
        }

        // Generate new random numbers and reset game state
        generateRandomNumbers();
    }
}