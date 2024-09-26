package com.android.lab_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_page_5 extends AppCompatActivity {

    private boolean isForward = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_5);

        final View square = findViewById(R.id.square);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.drop_and_scale);
        final Animation reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.drop_and_scale_reverse);
        square.setOnClickListener(v -> {
            if (isForward) {
                square.startAnimation(animation);
            } else {
                square.startAnimation(reverseAnimation);
            }
            isForward = !isForward;
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}