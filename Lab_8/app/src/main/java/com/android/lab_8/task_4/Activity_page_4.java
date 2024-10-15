package com.android.lab_8.task_4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_8.Activity_main;
import com.android.lab_8.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_page_4 extends AppCompatActivity {

    private EditText inputText;
    private TextView textPutResult;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_4);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        inputText = findViewById(R.id.input_text);
        textPutResult = findViewById(R.id.text_put_result);
        Button buttonPut = findViewById(R.id.button_put);

        client = new OkHttpClient();

        buttonPut.setOnClickListener(view -> sendPutRequest());
    }

    private void sendPutRequest() {
        String textToSend = inputText.getText().toString();

        if (textToSend.isEmpty()) {
            Toast.makeText(Activity_page_4.this, "Please enter text", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody formBody = new FormBody.Builder()
                .add("text", textToSend)
                .build();

        // PUT-запрос
        Request request = new Request.Builder()
                .url("http://httpbin.org/put")
                .put(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(Activity_page_4.this, "Request failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Request failed", e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        System.out.println(jsonObject.toString(4));
                        JSONObject formObject = jsonObject.getJSONObject("form");
                        final String formValue = formObject.getString("text");

                        runOnUiThread(() -> textPutResult.setText("Form value: " + formValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(Activity_page_4.this, "Failed to parse JSON", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Activity_page_4.this, "Error in response", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}