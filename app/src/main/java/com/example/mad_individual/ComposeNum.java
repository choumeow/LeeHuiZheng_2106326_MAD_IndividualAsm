package com.example.mad_individual;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ComposeNum extends AppCompatActivity {

    private TextView totalTextView, firstNumTextView, secondNumTextView;
    private Button num1, num2, num3, num4, num5, num6, undoButton;
    private int targetTotal;
    private int selectedCount = 0;
    private int firstSelected = 0;
    private int secondSelected = 0;
    private int roundsPlayed = 0;
    private int totalPoints = 0;
    private List<Integer> availableNumbers = new ArrayList<>();
    private Button lastSelectedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_num);

        // Initialize views
        totalTextView = findViewById(R.id.textView9);
        firstNumTextView = findViewById(R.id.textView10);
        secondNumTextView = findViewById(R.id.textView11);
        num1 = findViewById(R.id.button16);
        num2 = findViewById(R.id.button2);
        num3 = findViewById(R.id.button19);
        num4 = findViewById(R.id.button18);
        num5 = findViewById(R.id.button15);
        num6 = findViewById(R.id.button17);
        undoButton = findViewById(R.id.button_undo);

        // Set up the game
        setupNewRound();

        // Set click listeners
        num1.setOnClickListener(v -> handleNumberSelection(num1));
        num2.setOnClickListener(v -> handleNumberSelection(num2));
        num3.setOnClickListener(v -> handleNumberSelection(num3));
        num4.setOnClickListener(v -> handleNumberSelection(num4));
        num5.setOnClickListener(v -> handleNumberSelection(num5));
        num6.setOnClickListener(v -> handleNumberSelection(num6));

        undoButton.setOnClickListener(v -> undoLastSelection());
    }

    private void setupNewRound() {
        // Reset selection
        selectedCount = 0;
        firstSelected = 0;
        secondSelected = 0;
        firstNumTextView.setText("");
        secondNumTextView.setText("");
        undoButton.setVisibility(View.GONE);
        lastSelectedButton = null;

        // Generate target and numbers
        Random random = new Random();
        targetTotal = random.nextInt(90) + 10;
        totalTextView.setText(String.valueOf(targetTotal));

        availableNumbers.clear();

        // Generate solution pair
        int firstNum = random.nextInt(targetTotal - 1) + 1;
        int secondNum = targetTotal - firstNum;
        availableNumbers.add(firstNum);
        availableNumbers.add(secondNum);

        // Add random numbers
        while (availableNumbers.size() < 6) {
            int newNum = random.nextInt(50) + 1;
            if (!availableNumbers.contains(newNum)) {
                availableNumbers.add(newNum);
            }
        }

        Collections.shuffle(availableNumbers);

        // Assign numbers to buttons
        num1.setText(String.valueOf(availableNumbers.get(0)));
        num2.setText(String.valueOf(availableNumbers.get(1)));
        num3.setText(String.valueOf(availableNumbers.get(2)));
        num4.setText(String.valueOf(availableNumbers.get(3)));
        num5.setText(String.valueOf(availableNumbers.get(4)));
        num6.setText(String.valueOf(availableNumbers.get(5)));

        enableAllNumberButtons();
    }

    private void handleNumberSelection(Button button) {
        int selectedNumber = Integer.parseInt(button.getText().toString());

        if (selectedCount == 0) {
            firstSelected = selectedNumber;
            firstNumTextView.setText(String.valueOf(firstSelected));
            selectedCount++;
            button.setEnabled(false);
            lastSelectedButton = button;
            undoButton.setVisibility(View.VISIBLE);
        } else if (selectedCount == 1) {
            secondSelected = selectedNumber;
            secondNumTextView.setText(String.valueOf(secondSelected));
            selectedCount++;
            disableAllNumberButtons();
            undoButton.setVisibility(View.GONE);

            if (firstSelected + secondSelected == targetTotal) {
                totalPoints += 20;
                Toast.makeText(this, "Correct! +20 points", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect! Try again", Toast.LENGTH_SHORT).show();
            }

            roundsPlayed++;
            if (roundsPlayed < 5) {
                button.postDelayed(this::setupNewRound, 1500);
            } else {
                showResultsDialog();
            }
        }
    }

    private void undoLastSelection() {
        if (selectedCount == 1) {
            firstNumTextView.setText("");
            firstSelected = 0;
            selectedCount--;
            if (lastSelectedButton != null) {
                lastSelectedButton.setEnabled(true);
            }
            undoButton.setVisibility(View.GONE);
            lastSelectedButton = null;
        }
    }

    private void disableAllNumberButtons() {
        num1.setEnabled(false);
        num2.setEnabled(false);
        num3.setEnabled(false);
        num4.setEnabled(false);
        num5.setEnabled(false);
        num6.setEnabled(false);
    }

    private void enableAllNumberButtons() {
        num1.setEnabled(true);
        num2.setEnabled(true);
        num3.setEnabled(true);
        num4.setEnabled(true);
        num5.setEnabled(true);
        num6.setEnabled(true);
    }

    private void showResultsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("You earned " + totalPoints + " points!");
        builder.setCancelable(false);

        builder.setPositiveButton("Play Again", (dialog, which) -> {
            roundsPlayed = 0;
            totalPoints = 0;
            setupNewRound();
        });

        builder.setNegativeButton("Back", (dialog, which) -> {
            finish();
        });

        builder.show();
    }
}