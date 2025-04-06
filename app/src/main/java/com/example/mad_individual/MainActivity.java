package com.example.mad_individual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find all buttons
        Button btnCompareNum = findViewById(R.id.btnCompareNum);
        Button btnOrderNum = findViewById(R.id.btnOrderNum);
        Button btnComposeNum = findViewById(R.id.btnComposeNum);

        // Set click listeners
        btnCompareNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompareNum.class);
                startActivity(intent);
            }
        });

        btnOrderNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderNum.class);
                startActivity(intent);
            }
        });

        btnComposeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ComposeNum.class);
                startActivity(intent);
            }
        });
    }
}