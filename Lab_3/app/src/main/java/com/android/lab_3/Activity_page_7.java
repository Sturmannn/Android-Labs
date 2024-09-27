package com.android.lab_3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_page_7 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_7);

        Spinner spinner = findViewById(R.id.spinner);
        String[] options = {"Опция 1", "Опция 2", "Опция 3", "Опция 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btnShowSelection = findViewById(R.id.btnShowSelection);
        btnShowSelection.setOnClickListener(v -> {
            String selectedItem = spinner.getSelectedItem().toString();
            Toast.makeText(getApplicationContext(), "Выбранный элемент: " + selectedItem, Toast.LENGTH_SHORT).show();
        });

        // ----------------- Переключатели --------------------------------

        SwitchCompat switchView = findViewById(R.id.switchControl);
        TextView switchStateText = findViewById(R.id.switchStateText);

        switchStateText.setText(switchView.isChecked() ? "Включен" : "Выключен");

        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchStateText.setText("Включен");
            } else {
                switchStateText.setText("Выключен");
            }
        });

        // ----------------- Ползунок --------------------------------

        SeekBar seekBar = findViewById(R.id.seekBar);
        TextView seekBarValueText = findViewById(R.id.seekBarValueText);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueText.setText("Текущее значение: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}