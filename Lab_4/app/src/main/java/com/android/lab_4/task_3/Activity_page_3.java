package com.android.lab_4.task_3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

import java.util.Calendar;

public class Activity_page_3 extends AppCompatActivity {

    private TextView textViewText;
    private TextView textViewDate;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_3);

        textViewText = findViewById(R.id.textViewText);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);

        Button buttonText = findViewById(R.id.textButton);
        buttonText.setOnClickListener(v -> showInputTextDialog());

        Button buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(v -> showDatePickerDialog());

        Button buttonTime = findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(v -> showTimePickerDialog());

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void showInputTextDialog() {
        final EditText input = new EditText(Activity_page_3.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(Activity_page_3.this)
                .setTitle("Введите текст")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String enteredText = input.getText().toString();
                    textViewText.setText(enteredText);
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_page_3.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textViewDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_page_3.this,
                (view, selectedHour, selectedMinute) -> {
                    String selectedTime = selectedHour + ":" + String.format("%02d", selectedMinute);
                    textViewTime.setText(selectedTime);
                }, hour, minute, true);

        timePickerDialog.show();
    }
}