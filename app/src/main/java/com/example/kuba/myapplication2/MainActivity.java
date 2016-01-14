package com.example.kuba.myapplication2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.kuba.myapplication2.Services.ToastCreator;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ToastCreator toastCreator = new ToastCreator();
    private BluetoothAdapter bluetoothAdapter;
    public BluetoothDevice myDevice = null;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;
    String deviceName = "HC-06";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        View view = View.inflate(context, R.layout.activity_main, null);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        turnOn();
    }

    public void turnOn() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            toastCreator.createToast(getApplicationContext(), "Bluetooth adapter turned on");
        }
    }

    public void turnOff(View view) throws IOException {
        outputStream.close();
        bluetoothSocket.close();
        bluetoothAdapter.disable();
        toastCreator.createToast(getApplicationContext(), "Turned off");
    }

    public void connecting(View view) throws IOException {
        Context context = getApplicationContext();
        turnOn();
        findBT();
        try {
            openBT();
        }
        catch (IOException e){
            return;
        }
    }

    void findBT() throws IOException {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null) {
            for (BluetoothDevice device : devices) {
                if (deviceName.equals(device.getName())) {
                    myDevice = device;
                    toastCreator.createToast(getApplicationContext(), "Paired Bluetooth Device Found");
                    break;
                }
            }
        }
    }

    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");       //Standard SerialPortService ID
        bluetoothSocket = myDevice.createRfcommSocketToServiceRecord(uuid);
        bluetoothSocket.connect();
        outputStream = bluetoothSocket.getOutputStream();
        toastCreator.createToast(getApplicationContext(), "Bluetooth Connection Created");
    }

    public void deactivateLock(View view) throws IOException {
        outputStream.write("2".getBytes());
        toastCreator.createToast(getApplicationContext(), "Deactivated");
    }

    public void activateLock(View view) throws IOException {
        outputStream.write("1".getBytes());
        toastCreator.createToast(getApplicationContext(), "Activated");
    }
}