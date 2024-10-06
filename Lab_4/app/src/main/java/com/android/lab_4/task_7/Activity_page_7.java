package com.android.lab_4.task_7;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public class Activity_page_7 extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView textView;
    private Button openBottomMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_7);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        textView = findViewById(R.id.textView);
        openBottomMenuButton = findViewById(R.id.openBottomMenuButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                textView.setText("Вы выбрали: " + item.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                textView.setText("Вы выбрали: " + item.getTitle());
//                return true;
//            }
//        });
        openBottomMenuButton.setOnClickListener(view -> openBottomSheetMenu());
    }

    private void openBottomSheetMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.activity_page_7_bottom_sheet_menu, null);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        // Обработка нажатий на элементы нижнего меню
        bottomSheetView.findViewById(R.id.itemA).setOnClickListener(v -> {
            textView.setText("Нижний Элемент A");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.itemB).setOnClickListener(v -> {
            textView.setText("Нижний Элемент B");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.itemC).setOnClickListener(v -> {
            textView.setText("Нижний Элемент C");
            bottomSheetDialog.dismiss();
        });
    }
}