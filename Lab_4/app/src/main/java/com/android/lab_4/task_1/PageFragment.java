package com.android.lab_4.task_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.lab_4.R;

public class PageFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";

    public static PageFragment newInstance(int pageNumber) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_task_1, container, false);

        // Получаем номер страницы из аргументов
        int pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);

        // Отображаем номер страницы
        TextView pageTextView = view.findViewById(R.id.pageNumberText);
        pageTextView.setText("Страница: " + pageNumber);

        return view;
    }
}
