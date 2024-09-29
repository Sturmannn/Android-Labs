package com.android.lab_4.task_2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_4.R;

public class Activity_page_2_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_2_main);

        // Загрузка первого фрагмента при запуске приложения
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FirstFragment(), "FirstFragment")
                    .commit();
        }
    }
}