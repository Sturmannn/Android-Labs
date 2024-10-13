package com.android.lab_6.task_1;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.animation.Animator;
import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_6.Activity_main;
import com.android.lab_6.R;

public class Activity_page_1 extends AppCompatActivity {

    private View redLight;
    private View yellowLight;
    private View greenLight;
    private Handler handler;
    private int currentLight = 0; // 0 - красный, 1 - жёлтый, 2 - зелёный
    private boolean isRunning = false;
    private Runnable trafficLightRunnable;
    private Button toggleButton;

    private ValueAnimator animator;
    private ImageView walkingMan;
    private float currentPosition = 0; // Текущее положение человечка
    private boolean movingRight = true; // Флаг направления (true - вправо, false - влево)

    private TrafficLight trafficLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_1);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

//        redLight = findViewById(R.id.redLight);
//        yellowLight = findViewById(R.id.yellowLight);
//        greenLight = findViewById(R.id.greenLight);
        toggleButton = findViewById(R.id.toggleButton);
        walkingMan = findViewById(R.id.walkingMan);
        trafficLight = findViewById(R.id.trafficLight);

        handler = new Handler();
        trafficLightRunnable = new Runnable() {
            @Override
            public void run() {
                updateLights();
                handler.postDelayed(this, 1000);
            }
        };

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stopTrafficLightCycle();
                } else {
                    startTrafficLightCycle();
                }
            }
        });

    }

    private void startTrafficLightCycle() {
        handler.postDelayed(trafficLightRunnable, 500);
        isRunning = true;
        toggleButton.setText("Pause");
    }

    private void stopTrafficLightCycle() {
        handler.removeCallbacks(trafficLightRunnable);
        isRunning = false;
        toggleButton.setText("Start");
    }


    private void updateLights() {
//        redLight.setBackgroundResource(R.drawable.circle_shape_black);
//        yellowLight.setBackgroundResource(R.drawable.circle_shape_black);
//        greenLight.setBackgroundResource(R.drawable.circle_shape_black);
        trafficLight.setDefaultColor();

        switch (currentLight) {
            case 0:
//                redLight.setBackgroundResource(R.drawable.circle_shape_red);
                trafficLight.showRed();
                walkingMan.setImageResource(R.drawable.standing_man); // Статичный человечек
                if (animator != null && animator.isRunning()) {
                    animator.pause(); // Остановить анимацию
                }
                currentLight = 1;
                break;
            case 1:
//                yellowLight.setBackgroundResource(R.drawable.circle_shape_yellow);
                trafficLight.showYellow();
                walkingMan.setImageResource(R.drawable.standing_man); // Статичный человечек
                if (animator != null && animator.isRunning()) {
                    animator.pause(); // Остановить анимацию
                }
                currentLight = 2;
                break;
            case 2:
//                greenLight.setBackgroundResource(R.drawable.circle_shape_green);
                trafficLight.showGreen();
                walkingMan.setImageResource(R.drawable.walking_man); // Бегущий человечек
                startOrResumeAnimation(); // Запустить или продолжить анимацию
                currentLight = 0;
                break;
        }
    }

    private void startOrResumeAnimation() {
        final float screenWidth = getResources().getDisplayMetrics().widthPixels - walkingMan.getWidth();

        if (animator == null) {
            // Инициализируем анимацию
            animator = ValueAnimator.ofFloat(0, screenWidth);
            animator.setDuration(2000); // Длительность анимации
            animator.setInterpolator(new LinearInterpolator()); // Плавное движение

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentPosition = (float) animation.getAnimatedValue();
                    walkingMan.setTranslationX(currentPosition); // Перемещение человечка

                    // Проверяем, достиг ли человечек края экрана
                    if (currentPosition >= screenWidth && movingRight) {
                        // Меняем направление движения на обратное (влево)
                        movingRight = false;
                        walkingMan.setScaleX(-1); // Отзеркаливание изображения для обратного движения
                        restartAnimation(currentPosition, 0);
                    } else if (currentPosition <= 0 && !movingRight) {
                        // Меняем направление движения на вправо
                        movingRight = true;
                        walkingMan.setScaleX(1); // Отзеркаливание изображения для прямого движения
                        restartAnimation(currentPosition, screenWidth);
                    }
                }
            });

            animator.start(); // Запуск анимации
        } else {
            // Если анимация уже существует, возобновляем
            animator.resume();
        }
    }

    // Метод для перезапуска анимации с новым направлением
    private void restartAnimation(float start, float end) {
        if (animator != null) {
            animator.cancel(); // Останавливаем текущую анимацию
        }

        // Задаем новый диапазон анимации
        animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(2000); // Задаем новую длительность
        animator.setInterpolator(new LinearInterpolator()); // Плавное движение

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPosition = (float) animation.getAnimatedValue();
                walkingMan.setTranslationX(currentPosition); // Обновляем позицию человечка
            }
        });

        // Проверяем старт и конец анимации
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Анимация завершена, запускаем её снова в нужном направлении
                if (!movingRight) {
                    movingRight = true;
                    walkingMan.setScaleX(1); // Отзеркаливание изображения для прямого движения
                    // Если двигаемся влево, снова запускаем анимацию вправо
                    restartAnimation(currentPosition, getResources().getDisplayMetrics().widthPixels - walkingMan.getWidth());
                } else {
                    movingRight = false;
                    walkingMan.setScaleX(-1); // Отзеркаливание изображения для обратного движения
                    // Если двигаемся вправо, снова запускаем анимацию влево
                    restartAnimation(currentPosition, 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start(); // Запуск новой анимации
    }

}
