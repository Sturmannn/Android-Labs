package com.android.bluetooth_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<String> {

    public MessageAdapter(@NonNull Context context, @NonNull List<String> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String message = getItem(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(message);

        return convertView;
    }
}
