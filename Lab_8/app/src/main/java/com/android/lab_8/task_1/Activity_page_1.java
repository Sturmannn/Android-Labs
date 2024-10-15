package com.android.lab_8.task_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.lab_8.Activity_main;
import com.android.lab_8.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Activity_page_1 extends AppCompatActivity {

    private TextView textIp;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_1);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        textIp = findViewById(R.id.text_ip);
        Button buttonGetIp = findViewById(R.id.button_get_ip);

        client = new OkHttpClient();

        buttonGetIp.setOnClickListener(view -> fetchIpAddress());

    }

    private void fetchIpAddress() {
        Request request = new Request.Builder()
                .url("http://httpbin.org/ip")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(Activity_page_1.this, "Request failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Activity_page_1", "Request failed", e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        final String ipAddress = jsonObject.getString("origin");

                        runOnUiThread(() -> textIp.setText(ipAddress));
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(Activity_page_1.this, "Failed to parse JSON", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Activity_page_1.this, "Error in response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}