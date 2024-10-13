package com.android.lab_6.task_2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.lab_6.Activity_main;
import com.android.lab_6.R;

public class Activity_page_2 extends AppCompatActivity {

    private TextView movableText;
    private View menuButton;
    private float initialY;
    private float currentY;
    private ObjectAnimator moveDownAnimator;
    private ObjectAnimator rotateAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_2);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        movableText = findViewById(R.id.movableText);
        menuButton = findViewById(R.id.menuButton);

        // Запоминаем начальное положение текста
        movableText.post(() -> initialY = movableText.getY());

        movableText.setOnTouchListener(new View.OnTouchListener() {
            private boolean isMovingDown = true;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Начинаем анимацию перемещения вниз при удерживании
                        animateText();
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Останавливаем анимацию при отпускании
                        cancelAnimation();
                        // Возвращаем текст на текущей позиции обратно наверх
                        resetTextPosition();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Пока удерживаем, анимация продолжается
                        return true;

                    default:
                        return false;
                }
            }

            private void animateText() {
                float targetY = menuButton.getY() - movableText.getHeight();

                // Анимация перемещения текста вниз
                moveDownAnimator = ObjectAnimator.ofFloat(movableText, "y", isMovingDown ? targetY : initialY);
                moveDownAnimator.setDuration(2500); // Длительность анимации
                moveDownAnimator.start();

                // Анимация поворота текста на 180 градусов
                rotateAnimator = ObjectAnimator.ofFloat(movableText, "rotation", isMovingDown ? 180 : 0);
                rotateAnimator.setDuration(2500); // Длительность анимации
                rotateAnimator.start();

                movableText.setTextColor(isMovingDown ? Color.BLUE : Color.BLACK);
                isMovingDown = !isMovingDown;  // Меняем направление при повторной активации
            }

            private void cancelAnimation() {
                if (moveDownAnimator != null && moveDownAnimator.isRunning()) {
                    moveDownAnimator.cancel();  // Останавливаем анимацию
                }
                if (rotateAnimator != null && rotateAnimator.isRunning()) {
                    rotateAnimator.cancel();  // Останавливаем анимацию поворота
                }
                // Сохраняем текущее положение текста
                currentY = movableText.getY();
            }

            private void resetTextPosition() {
                // Возвращаем текст наверх (в зависимости от того, где отпустили)
                moveDownAnimator = ObjectAnimator.ofFloat(movableText, "y", currentY, initialY);
                moveDownAnimator.setDuration(1000);
                moveDownAnimator.start();

                // Возвращаем поворот текста
                rotateAnimator = ObjectAnimator.ofFloat(movableText, "rotation", 0);
                rotateAnimator.setDuration(1000);
                rotateAnimator.start();

                // Возвращаем цвет текста
                movableText.setTextColor(Color.BLACK);
            }
        });
    }
}