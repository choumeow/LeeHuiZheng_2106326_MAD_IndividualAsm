package com.example.mad_individual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_individual.MainActivity;

import java.util.Random;

public class CompareNum extends AppCompatActivity {

    private TextView textViewA, textViewB, scoreView, roundView;
    private Button greaterButton, lessButton;
    private int score = 0;
    private int currentRound = 1;
    private final int TOTAL_ROUNDS = 10;
    private int numberA, numberB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_num);

        // Initialize views
        textViewA = findViewById(R.id.textView);
        textViewB = findViewById(R.id.textView4);
        scoreView = findViewById(R.id.scoreView);
        roundView = findViewById(R.id.roundView);
        greaterButton = findViewById(R.id.button4);
        lessButton = findViewById(R.id.button5);

        // Set initial score and round
        updateScoreAndRound();

        // Generate first pair of numbers
        generateNewNumbers();

        // Set button click listeners
        greaterButton.setOnClickListener(v -> checkAnswer(true));
        lessButton.setOnClickListener(v -> checkAnswer(false));
    }

    private void generateNewNumbers() {
        Random random = new Random();
        numberA = random.nextInt(90) + 10; // 10-99
        numberB = random.nextInt(90) + 10; // 10-99

        // Make sure numbers are different
        while (numberA == numberB) {
            numberB = random.nextInt(90) + 10;
        }

        textViewA.setText(String.valueOf(numberA));
        textViewB.setText(String.valueOf(numberB));
    }

    private void checkAnswer(boolean isGreaterSelected) {
        boolean isCorrect = false;

        if (numberA > numberB && isGreaterSelected) {
            isCorrect = true;
        } else if (numberA < numberB && !isGreaterSelected) {
            isCorrect = true;
        }

        if (isCorrect) {
            score += 10;
            Toast.makeText(this, "Correct! +10 points", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Incorrect! Try again", Toast.LENGTH_SHORT).show();
        }


        currentRound++;
        updateScoreAndRound();

        if (currentRound <= TOTAL_ROUNDS) {
            generateNewNumbers();
        } else {
            showFinalScore();
        }
    }

    private void updateScoreAndRound() {
        scoreView.setText("Score: " + score);
        roundView.setText("Round: " + currentRound + "/" + TOTAL_ROUNDS);
    }

    private void showFinalScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("Your total score: " + score);

        builder.setPositiveButton("Play Again", (dialog, which) -> resetGame());
        builder.setNegativeButton("Back", (dialog, which) -> {
            Intent intent = new Intent(CompareNum.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void resetGame() {
        score = 0;
        currentRound = 1;
        updateScoreAndRound();
        generateNewNumbers();
    }
}