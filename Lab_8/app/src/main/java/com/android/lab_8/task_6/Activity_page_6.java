package com.android.lab_8.task_6;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.lab_8.Activity_main;
import com.android.lab_8.R;

import androidx.appcompat.app.AppCompatActivity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_page_6 extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private TextView textViewNfcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_6);

        findViewById(R.id.menuButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_main.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        textViewNfcInfo = findViewById(R.id.textViewNfcInfo);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC не поддерживается на этом устройстве.", Toast.LENGTH_SHORT).show();
            finish(); // Закрыть приложение
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Проверяем, если NFC включен
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast.makeText(this, "Пожалуйста, включите NFC.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Проверяем, что это намерение связано с NFC
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                displayNfcTagInfo(tag);
            }
        }
    }

    private void displayNfcTagInfo(Tag tag) {
        StringBuilder sb = new StringBuilder("NFC Tag Detected:\n");
        sb.append("ID: ").append(bytesToHex(tag.getId())).append("\n");

        // Получение технологий, поддерживаемых тегом
        String[] techList = tag.getTechList();
        sb.append("Technologies:\n");
        for (String tech : techList) {
            sb.append(tech).append("\n");
        }

        textViewNfcInfo.setText(sb.toString());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}