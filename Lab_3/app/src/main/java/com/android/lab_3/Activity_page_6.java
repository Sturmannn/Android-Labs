package com.android.lab_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_page_6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_6);

        // Найдем TimePicker и кнопку
        TimePicker timePicker = findViewById(R.id.timePicker);
        Button btnShowTime = findViewById(R.id.btnShowTime);

        timePicker.setIs24HourView(true);

        // Обработчик нажатия кнопки для времени
        btnShowTime.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            String selectedTime = String.format("%02d:%02d", hour, minute);

            Toast.makeText(getApplicationContext(), "Выбранное время: " + selectedTime, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}