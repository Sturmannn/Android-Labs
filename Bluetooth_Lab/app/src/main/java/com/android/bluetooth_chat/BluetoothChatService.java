package com.android.bluetooth_chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.Manifest;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.widget.TextView;

public class BluetoothChatService {

    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private AcceptThread acceptThread;
    private boolean isConnected = false;
    private AppCompatActivity activity;
    private Button discoverDevicesButton;

    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;

    private static final int REQUEST_BLUETOOTH_SCAN = 1;

    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket;

    public BluetoothChatService(Handler handler, AppCompatActivity activity, Button discoverDevicesButton) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = handler;
        this.activity = activity;
        this.discoverDevicesButton = discoverDevicesButton;
    }

    public void connect(BluetoothDevice device, @NonNull AppCompatActivity activity) {
        this.activity = activity;

        if (!checkBluetoothPermissions()) {
            requestBluetoothPermissions(activity);
            return;
        }

        if (isConnected) {
            Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Already connected to another device.");
            handler.sendMessage(message);
            return;
        }

        Log.d("BluetoothChatService", "Connecting to device...");
        connectThread = new ConnectThread(device);
        Message connectingMessage = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Connecting to device...");
        handler.sendMessage(connectingMessage);
        Log.d("BluetoothChatService", "Starting ConnectThread...");
        connectThread.start();
    }

    String getConnectedDeviceName() {
        if (socket != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BluetoothChatService", "Bluetooth permissions are required for getting connected device name");
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Bluetooth permissions are required for getting connected device name");
                handler.sendMessage(message);
                return null;
            }
            return socket.getRemoteDevice().getName();
        }
        return null;
    }

    public void startServer(AppCompatActivity activity) {
        this.activity = activity;

        if (!checkBluetoothPermissions()) {
            requestBluetoothPermissions(activity);
            return;
        }

        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    private boolean checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestBluetoothPermissions(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    1);
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                Log.d("BluetoothChatService", "ServerSocket closed.");
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "ServerSocket closed.");
                handler.sendMessage(message);
            } else {
                Log.d("BluetoothChatService", "ServerSocket was already null or closed.");
            }
        } catch (IOException e) {
            Log.d("BluetoothChatService", "Failed to close 'serverSocket': " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class AcceptThread extends Thread {
        public AcceptThread() {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_SCAN);
                    Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Bluetooth permissions are required for accept thread");
                    handler.sendMessage(message);
                    return;
                }

                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothChat", MY_UUID);

                Log.d("BluetoothChatService", "ServerSocket created: " + serverSocket);
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "ServerSocket created");
                handler.sendMessage(message);

            } catch (IOException e) {
                Log.d("BluetoothChatService", "Failed to create 'accept' socket: " + e.getMessage());
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to create 'accept' socket");
                handler.sendMessage(message);
            }
        }

        @Override
        public void run() {
            try {
                // Используем уже существующую переменную socket, не создавая её заново
                socket = serverSocket.accept();
                Log.d("BluetoothChatService", "Connection accepted: " + socket);

                if (socket != null) {
                    connected(socket);

                    // Отправка сообщения через handler
                    Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Connection accepted");
                    handler.sendMessage(message);

                    // Обновление UI в главном потоке
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            discoverDevicesButton.setEnabled(false);
                        }
                    });
                }
            } catch (IOException e) {
                Log.d("BluetoothChatService", "Failed to accept connection: " + e.getMessage());

                // Отправка сообщения об ошибке
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to accept connection");
                handler.sendMessage(message);
            } finally {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView connectedDeviceNameTextView = activity.findViewById(R.id.connectedDeviceNameTextView);
                        connectedDeviceNameTextView.setText(getConnectedDeviceName());
                    }
                });
                // Закрытие серверного сокета вне метода run()
                closeServerSocket();
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice device;
//        private BluetoothSocket socket;

        public ConnectThread(BluetoothDevice device) {
            this.device = device;
            BluetoothSocket tmp = null;

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    handler.post(() -> Toast.makeText(activity, "Bluetooth permissions are required", Toast.LENGTH_SHORT).show());
                    return;
                }
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d("BluetoothChatService", "Connect Socket created: " + tmp);
            } catch (IOException e) {
                Log.d("BluetoothChatService", "Failed to create 'connect' socket: " + e.getMessage());
            }

            socket = tmp;
        }

        @Override
        public void run() {
            Log.d("BluetoothChatService", "Running ConnectThread...");

            Message connectingMessage = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Connecting to device...");
            handler.sendMessage(connectingMessage);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d("BluetoothChatService", "Bluetooth permissions are required for connect thread");
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Bluetooth permissions are required for connect thread");
                handler.sendMessage(message);
                return;
            }
            bluetoothAdapter.cancelDiscovery();

            try {
                if (socket != null) {
                    socket.connect();
                    Log.d("BluetoothChatService", "Connected to device (before 'connected' constructor): " + device.getName());
                    connected(socket);
                    Log.d("BluetoothChatService", "Connected to device (after 'connected' constructor): " + device.getName());

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            discoverDevicesButton.setEnabled(false);
                        }
                    });
                }
            } catch (IOException e) {
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to connect to device");
                handler.sendMessage(message);
                Log.d("BluetoothChatService", "Failed to connect to device: " + device.getName());
                try {
                    if (socket != null) {
                        Log.d("BluetoothChatService", "Closing socket... (ConnectThread)");
                        socket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket connectedSocket;
        private final InputStream inStream;
        private final OutputStream outStream;

        public ConnectedThread(BluetoothSocket socket) {
            connectedSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = connectedSocket.getInputStream();
                tmpOut = connectedSocket.getOutputStream();
                Log.d("BluetoothChatService", "Input/output stream created (ConnectedThread)");
            } catch (IOException e) {
                Log.d("BluetoothChatService", "Failed to get input/output stream: " + e.getMessage());
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to get input/output stream");
                handler.sendMessage(message);
                e.printStackTrace();
            }

            inStream = tmpIn;
            outStream = tmpOut;

//            // Пробная отправка и чтение для тестирования потоков

//            try {
//                Log.d("BluetoothChatService", "START Sending test message...");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    Log.d("BluetoothChatService", "Bluetooth permissions are required for test send/receive");
//                    Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Bluetooth permissions are required for test send/receive");
//                    handler.sendMessage(message);
//                    return;
//                }
//                String testMessage = "Test message" + socket.getRemoteDevice().getName();
//                outStream.write(testMessage.getBytes()); // Отправка тестового сообщения
//                Log.d("BluetoothChatService", "Test message sent: " + testMessage);
//
//                // Пробное чтение после отправки
//                byte[] buffer = new byte[1024];
//                int bytes = inStream.read(buffer);
//                String receivedMessage = new String(buffer, 0, bytes);
//                Log.d("BluetoothChatService", "Test message received: " + receivedMessage);
//
//                handler.obtainMessage(BluetoothChatService.MESSAGE_WRITE, -1, -1, testMessage.getBytes()).sendToTarget();
//                // Передача принятого сообщения в основной поток для отображения
//                handler.obtainMessage(BluetoothChatService.MESSAGE_READ, bytes, -1, buffer.clone()).sendToTarget();
//            } catch (IOException e) {
//                Log.d("BluetoothChatService", "Error during test send/receive: " + e.getMessage());
//                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Test send/receive failed");
//                handler.sendMessage(message);
//                e.printStackTrace();
//            }
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            int i = 0;
            while (true) {
                try {
                    if (socket.isConnected()) {
                        System.out.println("ConnectedThread iteration: " + i++);
                        bytes = inStream.read(buffer);
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer.clone()).sendToTarget();
                    } else {
                        Log.d("BluetoothChatService", "Socket is not connected.");
                        break;
                    }

                } catch (IOException e) {
                    Log.d("BluetoothChatService", "Failed to read from input stream: " + e.getMessage());
                    Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to read from input stream");
                    handler.sendMessage(message);
                    break;
                }
            }
                // Закрытие соединения при выходе из цикла чтения
                closeConnection();
        }

        private void closeConnection() {
            try {
                if (connectedThread != null) {
                    connectedThread.interrupt();
                    connectedThread = null;
                }
                if (socket != null) {
                    socket.close();
                    Log.d("BluetoothChatService", "Connection closed (ConnectedThread)");
                }
                isConnected = false;
            } catch (IOException e) {
                Log.e("BluetoothChatService", "Error closing connection", e);
            }
        }

        public void write(byte[] bytes) {
            if (outStream == null) {
                Log.d("BluetoothChatService", "Output stream is null, cannot write data.");
                return;
            }

            try {
                Log.d("BluetoothChatService", "Writing to output stream: " + new String(bytes));
                outStream.write(bytes);
                handler.obtainMessage(MESSAGE_WRITE, -1, -1, bytes).sendToTarget();
            } catch (IOException e) {
                Log.d("BluetoothChatService", "Failed to write to output stream: " + e.getMessage());
                Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Failed to write to output stream");
                handler.sendMessage(message);
                Log.e("BluetoothChatService", "IOException during write", e);
            }
        }
    }

    private void connected(BluetoothSocket socket) {
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        isConnected = true;
        Log.d("BluetoothChatService", "ConnectedThread start running...");

//        Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Connected to device");
//        handler.sendMessage(message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d("BluetoothChatService", "Bluetooth permissions are required for connected thread");
            return;
        }
        Log.d("BluetoothChatService", "Connected to device: " + socket.getRemoteDevice().getName());
    }

    public void stop() {
        if (connectThread != null) {
            connectThread.interrupt();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.interrupt();
            connectedThread = null;
        }
        isConnected = false;
    }

    public void write(byte[] out) {
        if (isConnected && connectedThread != null) {
            connectedThread.write(out);
        } else {
            Log.d("BluetoothChatService", "Not connected");
            Message message = handler.obtainMessage(BluetoothChatService.MESSAGE_TOAST, "Not connected");
            handler.sendMessage(message);
        }
    }
}
