package com.android.lab_4.task_9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class Activity_page_9 extends AppCompatActivity {

    private TextView counterTextView;
    private Button incrementButton;
    private Button resetButton;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_9);

//        counterTextView = findViewById(R.id.counterTextView);
//        incrementButton = findViewById(R.id.incrementButton);
//        resetButton = findViewById(R.id.resetButton);
//
//        incrementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                counter++;
//                counterTextView.setText(String.valueOf(counter));
//            }
//        });
//
//        resetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                counter = 0;  // Сбрасываем счёт
//                counterTextView.setText(String.valueOf(counter));
//            }
//        });

        // Кнопка для запуска Foreground Service
        Button btnStartService = findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, CounterService.class);
            startService(serviceIntent);
        });

        // Кнопка для остановки Foreground Service
        Button btnStopService = findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, CounterService.class);
            stopService(serviceIntent);
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}