package com.android.lab_4.task_4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_page_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_4);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("17.02.2017", "Сделать дело"));
        taskList.add(new Task("17.02.2017", "Гулять смело"));
        taskList.add(new Task("16.02.2017", "Прочитать книгу"));
        taskList.add(new Task("15.02.2017", "Сходить на учёбу"));

        TaskAdapter_task4 taskAdapterTask4 = new TaskAdapter_task4(taskList);
        recyclerView.setAdapter(taskAdapterTask4);
    }
}