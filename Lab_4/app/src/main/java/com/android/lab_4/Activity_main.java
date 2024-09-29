package com.android.lab_4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.android.lab_4.task_1.Activity_page_1_1;
import com.android.lab_4.task_2.Activity_page_2_main;
import com.android.lab_4.task_3.Activity_page_3;
import com.android.lab_4.task_4.Activity_page_4;
import com.android.lab_4.task_5.Activity_page_5;
import com.android.lab_4.task_6.Activity_page_6;
import com.android.lab_4.task_7.Activity_page_7;
import com.android.lab_4.task_8.Activity_page_8;
import com.android.lab_4.task_9.Activity_page_9;

public class Activity_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonActivity1).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_1_1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_2_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity3).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_3.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity4).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_4.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity5).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_5.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity6).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_6.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.buttonActivity7).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_7.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.buttonActivity8).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_8.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.buttonActivity9).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_page_9.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}