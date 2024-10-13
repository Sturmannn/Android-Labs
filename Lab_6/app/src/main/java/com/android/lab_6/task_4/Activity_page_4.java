package com.android.lab_6.task_4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_6.Activity_main;
import com.android.lab_6.R;

public class Activity_page_4 extends AppCompatActivity {

    private HourView hourView;
    private MinuteView minuteView;
    private SecondView secondView;
    private Button startPauseButton;
    private Button resetButton;

    private boolean isRunning = false;
    private int seconds = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_4);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        hourView = findViewById(R.id.hourView);
        minuteView = findViewById(R.id.minuteView);
        secondView = findViewById(R.id.secondView);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetButton = findViewById(R.id.resetButton);

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        isRunning = true;
        startPauseButton.setText("Pause");
        handler.postDelayed(runnable, 1000);
    }

    private void pauseTimer() {
        isRunning = false;
        startPauseButton.setText("Resume");
        handler.removeCallbacks(runnable);
    }

    private void resetTimer() {
        isRunning = false;
        startPauseButton.setText("Start");
        handler.removeCallbacks(runnable);
        seconds = 0; // Сброс секунд
        updateTime(); // Обновление отображения времени
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            updateTime();
            handler.postDelayed(this, 1000);
        }
    };

    private void updateTime() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        hourView.setHour(hours);
        minuteView.setMinute(minutes);
        secondView.setSecond(secs);
    }
}