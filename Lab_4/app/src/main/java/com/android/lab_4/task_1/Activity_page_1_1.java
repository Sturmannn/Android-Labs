package com.android.lab_4.task_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class Activity_page_1_1 extends AppCompatActivity {

    private int stackDepth = 1;  // Изначально стек начинается с 1 (текущая активность)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_1_1);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        Button buttonForward = findViewById(R.id.buttonForward);

        buttonForward.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_page_1_1.this, StackActivity.class);
            intent.putExtra("STACK_DEPTH", stackDepth + 1);  // Передаем увеличенную глубину
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }
}
