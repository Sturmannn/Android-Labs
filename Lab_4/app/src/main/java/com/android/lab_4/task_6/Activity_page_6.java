package com.android.lab_4.task_6;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_page_6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_6);

        List<String> tasks = new ArrayList<>();
        tasks.add("Сделать дело");
        tasks.add("Прочитать книгу");
        tasks.add("Сходить на учёбу");

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TaskPagerAdapter adapter = new TaskPagerAdapter(tasks);
        viewPager.setAdapter(adapter);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}