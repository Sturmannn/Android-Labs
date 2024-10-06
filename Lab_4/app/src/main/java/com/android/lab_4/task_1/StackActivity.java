package com.android.lab_4.task_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_4.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StackActivity extends AppCompatActivity {

    private int stackDepth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack_activity);

        stackDepth = getIntent().getIntExtra("STACK_DEPTH", 1);

        TextView stackDepthText = findViewById(R.id.stackDepth);
        stackDepthText.setText("Глубина стека: " + stackDepth);

        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonForward = findViewById(R.id.buttonForward);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StackActivity.this, StackActivity.class);
                intent.putExtra("STACK_DEPTH", stackDepth + 1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}