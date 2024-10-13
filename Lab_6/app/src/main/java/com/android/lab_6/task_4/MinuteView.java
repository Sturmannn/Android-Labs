package com.android.lab_6.task_4;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class MinuteView extends AppCompatTextView {

    public MinuteView(Context context) {
        super(context);
    }

    public MinuteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMinute(int minute) {
        setText(String.format("%02d", minute));
    }
}