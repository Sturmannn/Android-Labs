package com.android.lab_2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_page_6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_6);

        Button openDialogButton = findViewById(R.id.openDialogButton);

        openDialogButton.setOnClickListener(v -> showCustomDialog());

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void showCustomDialog() {
        LayoutInflater inflater = LayoutInflater.from(Activity_page_6.this);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_page_6.this);
        builder.setTitle("Введите два числа");

        builder.setView(dialogView);

        EditText firstNumber = dialogView.findViewById(R.id.firstNumber);
        EditText secondNumber = dialogView.findViewById(R.id.secondNumber);

        builder.setPositiveButton("Подтвердить", (dialog, which) -> {
            String firstNumStr = firstNumber.getText().toString();
            String secondNumStr = secondNumber.getText().toString();

            if (!firstNumStr.isEmpty() && !secondNumStr.isEmpty()) {
                try {
                    int firstNum = Integer.parseInt(firstNumStr);
                    int secondNum = Integer.parseInt(secondNumStr);

                    int sum = firstNum + secondNum;
                    Toast.makeText(Activity_page_6.this, "Сумма: " + sum, Toast.LENGTH_LONG).show();

                } catch (NumberFormatException e) {
                    Toast.makeText(Activity_page_6.this, "Ошибка преобразования числа", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Activity_page_6.this, "Одно из полей пустое.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}