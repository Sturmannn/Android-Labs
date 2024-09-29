package com.android.lab_4.task_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.lab_4.Activity_main;
import com.android.lab_4.R;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.first_fragment_page_task_2, container, false);

        // Находим кнопки "Добавить страницу" и "Убрать страницу"
        Button addPageButton = view.findViewById(R.id.addPageButton);
        Button removePageButton = view.findViewById(R.id.removePageButton);

        // Установка действия для кнопки "Добавить страницу"
        addPageButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            SecondFragment secondFragment = new SecondFragment();

            // Скрываем первый объект, добавляем второй фрагмент и сохраняем первый в стеке
            transaction.hide(this);
            transaction.add(R.id.fragment_container, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Установка действия для кнопки "Убрать страницу"
        removePageButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(); // Убираем последний фрагмент из стека
        });

        view.findViewById(R.id.menuButton).setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            Intent intent = new Intent(requireActivity(), Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        return view;
    }
}