package com.android.lab_4.task_8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class Activity_page_8 extends AppCompatActivity {

    private ListView listView;
    private String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
    private int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_8);

        listView = findViewById(R.id.listView);

        // Создаем адаптер для ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_page_8_list_item, R.id.textItem, items);
        listView.setAdapter(adapter);

        // Регистрируем контекстное меню для ListView
        registerForContextMenu(listView);

        // Обработчик клика по элементу списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItemPosition = position;
                view.showContextMenu(); // Открытие контекстного меню при клике на элемент
            }
        });

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    // Создаем контекстное меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    // Обрабатываем выбор элемента контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedItemPosition >= 0) {
            int itemId = item.getItemId();  // Получаем ID выбранного элемента
            if (itemId == R.id.option_edit) {
                Toast.makeText(this, "Edit selected for item at position: " + selectedItemPosition, Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.option_delete) {
                Toast.makeText(this, "Delete selected for item at position: " + selectedItemPosition, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

}