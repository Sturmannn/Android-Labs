package com.android.lab_6.task_3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import androidx.annotation.Nullable;

import com.android.lab_6.R;

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        super(context);
        init(null);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);

            String text = typedArray.getString(R.styleable.CustomButton_buttonText);
            int color = typedArray.getColor(R.styleable.CustomButton_buttonColor, Color.GRAY);

            setText(text != null ? text : "Default Text");
            setBackgroundColor(color);

            typedArray.recycle();
        }
    }

    public void setButtonText(String text) {
        setText(text);
    }

    public void setButtonColor(int color) {
        setBackgroundColor(color);
    }
}