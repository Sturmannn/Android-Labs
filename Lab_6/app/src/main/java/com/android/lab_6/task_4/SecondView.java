package com.android.lab_6.task_4;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class SecondView extends AppCompatTextView {

    public SecondView(Context context) {
        super(context);
    }

    public SecondView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSecond(int second) {
        setText(String.format("%02d", second));
    }
}