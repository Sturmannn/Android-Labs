package com.android.lab_6.task_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.lab_6.Activity_main;
import com.android.lab_6.R;

public class Activity_page_5 extends AppCompatActivity {

    private PageStackHandler pageStackHandler;
    private TextView pageCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_5);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        pageCountTextView = findViewById(R.id.pageCountTextView);
        pageStackHandler = new PageStackHandler(getSupportFragmentManager());

        Button addPageButton = findViewById(R.id.addPageButton);
        Button removePageButton = findViewById(R.id.removePageButton);

        addPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newPage = new PageFragment(); // Ваш фрагмент страницы
                pageStackHandler.addPage(newPage);
                updatePageCount();
            }
        });

        removePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageStackHandler.removePage();
                updatePageCount();
            }
        });
    }

    private void updatePageCount() {
        String countText = "Added Pages: " + pageStackHandler.getAddedPagesCount() +
                "\nRemoved Pages: " + pageStackHandler.getRemovedPagesCount();
        pageCountTextView.setText(countText);
    }
}