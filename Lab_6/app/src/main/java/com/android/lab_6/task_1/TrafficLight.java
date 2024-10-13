package com.android.lab_6.task_1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_6.R;

public class TrafficLight extends LinearLayout {

    private View redLight;
    private View yellowLight;
    private View greenLight;

    public TrafficLight(Context context) {
        super(context);
        init(context);
    }

    public TrafficLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_traffic_light, this); // Подключаем разметку
        redLight = findViewById(R.id.redLight);
        yellowLight = findViewById(R.id.yellowLight);
        greenLight = findViewById(R.id.greenLight);
        setDefaultColor(); // Изначально все огни черные
    }

    public void setDefaultColor() {
        redLight.setBackgroundResource(R.drawable.circle_shape_black);
        yellowLight.setBackgroundResource(R.drawable.circle_shape_black);
        greenLight.setBackgroundResource(R.drawable.circle_shape_black);
    }

    public void showRed() {
//        setLightsColor(Color.BLACK);
        redLight.setBackgroundResource(R.drawable.circle_shape_red);
    }

    public void showYellow() {
//        setLightsColor(Color.BLACK);
        yellowLight.setBackgroundResource(R.drawable.circle_shape_yellow);
    }

    public void showGreen() {
//        setLightsColor(Color.BLACK);
        greenLight.setBackgroundResource(R.drawable.circle_shape_green);
    }
}