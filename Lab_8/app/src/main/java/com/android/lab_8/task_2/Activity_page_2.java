package com.android.lab_8.task_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_8.Activity_main;
import com.android.lab_8.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_page_2 extends AppCompatActivity {

    private TextView textPostResult;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_2);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        textPostResult = findViewById(R.id.text_post_result);
        Button buttonPost = findViewById(R.id.button_post);

        // Инициализация OkHttp клиента
        client = new OkHttpClient();

        // Установка действия на кнопку
        buttonPost.setOnClickListener(view -> sendPostRequest());
    }

    private void sendPostRequest() {

        RequestBody formBody = new FormBody.Builder()
                .add("key", "value")
                .build();

        Request request = new Request.Builder()
                .url("http://httpbin.org/post")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(Activity_page_2.this, "Request failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Request failed", e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> textPostResult.setText(responseData));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Activity_page_2.this, "Error in response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}