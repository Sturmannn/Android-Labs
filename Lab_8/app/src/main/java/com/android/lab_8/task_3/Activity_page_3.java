package com.android.lab_8.task_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_8.Activity_main;
import com.android.lab_8.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_page_3 extends AppCompatActivity {

    private ImageView imageView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_3);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        imageView = findViewById(R.id.image_view);
        Button buttonGetImage = findViewById(R.id.button_get_image);

        client = new OkHttpClient();

        buttonGetImage.setOnClickListener(view -> fetchImage());
    }

    private void fetchImage() {
        Request request = new Request.Builder()
                .url("http://httpbin.org/image/png")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(Activity_page_3.this, "Request failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Request failed", e);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    runOnUiThread(() -> imageView.setImageBitmap(bitmap));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Activity_page_3.this, "Error in response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}