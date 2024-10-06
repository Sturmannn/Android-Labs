package com.android.lab_4.task_9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class Activity_page_9 extends AppCompatActivity {

    private Button startServiceButton;
    private Button stopServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_9);

        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stopServiceButton);

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(Activity_page_9.this, CounterForegroundService.class);
                startService(serviceIntent);
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(Activity_page_9.this, CounterForegroundService.class);
                stopService(serviceIntent);
            }
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            stopServiceButton.performClick();
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}