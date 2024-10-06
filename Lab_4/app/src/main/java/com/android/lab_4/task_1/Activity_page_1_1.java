package com.android.lab_4.task_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class Activity_page_1_1 extends AppCompatActivity {

    private int pageCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_1_1);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        TextView stackDepthText = findViewById(R.id.stackDepthText);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnForward = findViewById(R.id.btnForward);

        // Установим первый фрагмент при запуске приложения
        if (savedInstanceState == null) {
            addNewFragment();
        }

        // Обработчик кнопки "Назад"
        btnBack.setOnClickListener(v -> {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                updateStackDepthText(stackDepthText);
            }
            else {
                Toast.makeText(Activity_page_1_1.this, "Нет страниц для возврата", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик кнопки "Вперёд"
        btnForward.setOnClickListener(v -> {
            addNewFragment();
            updateStackDepthText(stackDepthText);
        });

        // Обновляем текст глубины стека при запуске
        updateStackDepthText(stackDepthText);

        // Регистрация обработчика для системной кнопки "Назад"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                btnBack.performClick();
            }
        });
    }

    // Метод для добавления нового фрагмента в стек
    private void addNewFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right);

        // Создаем новый фрагмент
        PageFragment newFragment = PageFragment.newInstance(pageCounter);
        pageCounter++;

        // Добавляем фрагмент в контейнер и в стек
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Метод для обновления текста глубины стека
    private void updateStackDepthText(TextView stackDepthText) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Ожидание завершения всех фоновых транзакции
        fragmentManager.executePendingTransactions();
        int stackDepth = fragmentManager.getBackStackEntryCount();

        stackDepthText.setText("Глубина стека: " + stackDepth);
    }
}
