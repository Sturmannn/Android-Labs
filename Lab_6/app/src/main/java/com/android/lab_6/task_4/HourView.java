package com.android.lab_6.task_4;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class HourView extends AppCompatTextView {

    public HourView(Context context) {
        super(context);
    }

    public HourView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHour(int hour) {
        setText(String.format("%02d", hour));
    }
}