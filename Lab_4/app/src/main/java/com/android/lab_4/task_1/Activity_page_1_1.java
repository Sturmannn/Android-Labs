package com.android.lab_4.task_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
        });

        // Обработчик кнопки "Вперёд"
        btnForward.setOnClickListener(v -> {
            addNewFragment();
            updateStackDepthText(stackDepthText);
        });

        // Обновляем текст глубины стека при запуске
        updateStackDepthText(stackDepthText);
    }

    // Метод для добавления нового фрагмента в стек
    private void addNewFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

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
