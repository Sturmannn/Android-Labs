package com.android.lab_6.task_5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.lab_6.R;

public class PageFragment extends Fragment {

    private EditText inputField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        inputField = view.findViewById(R.id.inputField);
        Button showInputButton = view.findViewById(R.id.showInputButton);

        showInputButton.setOnClickListener(v -> {
            String inputText = inputField.getText().toString();
            Toast.makeText(getActivity(), "You entered: " + inputText, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}