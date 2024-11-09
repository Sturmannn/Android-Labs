package com.android.bluetooth_chat;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 2;
    private static final int REQUEST_BLUETOOTH_SCAN = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothChatService chatService;

    private EditText messageEditText;
    private Button sendButton, discoverButton;
    private ListView messagesListView;

    private final ArrayList<String> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;

    private ActivityResultLauncher<Intent> enableBtLauncher;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        discoverButton = findViewById(R.id.discoverDevicesButton);
        messagesListView = findViewById(R.id.messagesListView);

        // Настройка адаптера
        messageAdapter = new MessageAdapter(this, messages);
        messagesListView.setAdapter(messageAdapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Проверка поддержки Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
        }

        // Лаунчер для включения Bluetooth
        enableBtLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        setupChatService();
                    } else {
//                        Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                        showBluetoothDeniedDialog();
                    }
                }
        );

        // Обработчик кнопки отправки сообщений
        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });

        // Обработчик кнопки поиска устройств
        discoverButton.setOnClickListener(v -> discoverDevices());


        // Проверка и включение Bluetooth
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtLauncher.launch(enableBtIntent);
        } else {
            setupChatService();
        }

    }

    private void setupChatService() {

        // Проверяем, разрешено ли BLUETOOTH_SCAN (актуально для Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // Разрешение отсутствует, запрашиваем его
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.BLUETOOTH_SCAN},
                        REQUEST_BLUETOOTH_SCAN);
                return; // Останавливаем выполнение метода до получения разрешения
            }
        }

        chatService = new BluetoothChatService(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Log.d("BluetoothChatService", "Message in handler: " + msg.what + " " + msg.obj);
                switch (msg.what) {
                    case BluetoothChatService.MESSAGE_READ:
                        Log.d("BluetoothChatService", "Received: " + new String((byte[]) msg.obj, 0, msg.arg1));
                        String receivedMessage = new String((byte[]) msg.obj, 0, msg.arg1);
                        messages.add("Received: " + receivedMessage);
                        break;
                    case BluetoothChatService.MESSAGE_WRITE:
//                        Log.d("BluetoothChatService", "Sent: " + sentMessage);
                        Log.d("BluetoothChatService", "Sent: " + new String((byte[]) msg.obj));
                        String sentMessage = new String((byte[]) msg.obj);
                        messages.add("Sent: " + sentMessage);
                        break;
                    case BluetoothChatService.MESSAGE_TOAST:
//                        Log.d("BluetoothChatService", "Toast: " + msg.obj.toString());
                        Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                }
                messageAdapter.notifyDataSetChanged();
                return true;
            }
        }), this, discoverButton);

        // Запускаем сервер
        Log.d("MainActivity", "Starting server");
        chatService.startServer(this);
        Log.d("MainActivity", "Server started");
    }

//    По-моему здесь не предусмотрен случай, когда несколько раз нажимаешь на эту кнопку
    private void discoverDevices() {

        // Отключаем кнопку, пока идет поиск
        discoverButton.setEnabled(false);

        // Поиск подключенных устройств
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS);
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                connectToDevice(device);
                break; // Подключаемся к первому найденному устройству
            }
        } else {
            Toast.makeText(this, "No paired devices found", Toast.LENGTH_SHORT).show();
        }

        // Включаем кнопку обратно после завершения поиска
        discoverButton.setEnabled(true);
    }

    private void connectToDevice(BluetoothDevice device) {
        if (chatService != null) {
            chatService.connect(device, this);
        }
    }

    private void sendMessage(String message) {
        if (chatService != null) {
            chatService.write(message.getBytes());
            messages.add("Sent: " + message);
            messageAdapter.notifyDataSetChanged();
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Not connected to any device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatService != null) {
            chatService.stop();
        }
    }

    // Метод для показа диалога, если пользователь не разрешил включение Bluetooth
    private void showBluetoothDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Bluetooth Required")
                .setMessage("Bluetooth is required for this app to function. Please enable Bluetooth.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        enableBtLauncher.launch(enableBtIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено
                Log.d("MainActivity", "Разрешение BLUETOOTH_SCAN предоставлено");
            } else {
                // Разрешение отклонено
                Log.e("MainActivity", "Разрешение BLUETOOTH_SCAN отклонено");
                Toast.makeText(this, "Для работы Bluetooth необходимо разрешение", Toast.LENGTH_SHORT).show();
                setupChatService(); // Повторно вызываем метод для проверки разрешения
            }
        }
    }
}
