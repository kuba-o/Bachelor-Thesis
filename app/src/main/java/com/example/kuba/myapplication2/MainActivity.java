package com.example.kuba.myapplication2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    String deviceName;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = View.inflate(getApplicationContext(), R.layout.activity_main, null);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        turnOnBluetoothAdapter();
        disableButton(R.id.activate);
        disableButton(R.id.deactivate);
        disableButton(R.id.disconnect);
        deviceName = getString(R.string.device_name);
    }

    public void disableButton(int id){
        btn = (Button) findViewById(id);
        btn.setEnabled(false);
    }

    public void enableButton(int id){
        btn = (Button) findViewById(id);
        btn.setEnabled(true);
    }
    public void turnOnBluetoothAdapter() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            toastCreator.createToast(getApplicationContext(), "Bluetooth adapter turned on");
        }
    }

    public void turnOff(View view) throws IOException {
        bluetoothAdapter.disable();
        outputStream.close();
        bluetoothSocket.close();
        toastCreator.createToast(getApplicationContext(), "Turned off");
        disableButton(R.id.disconnect);
        disableButton(R.id.activate);
        disableButton(R.id.deactivate);
        enableButton(R.id.connect);
    }

    public void connecting(View view) throws IOException {
        turnOnBluetoothAdapter();
        try {
            findBluetooth();
            openBluetooth();
            disableButton(R.id.connect);
            enableButton(R.id.disconnect);
            enableButton(R.id.activate);
        }
        catch (IOException e) {
        }
    }

    void findBluetooth() throws IOException {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (deviceName.equals(device.getName())) {
                myDevice = device;
                toastCreator.createToast(getApplicationContext(), "Paired Bluetooth Device Found");
                break;
            }
        }
    }

    void openBluetooth() throws IOException {
        if (myDevice != null) {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");       //Standard SerialPortService ID
            bluetoothSocket = myDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            toastCreator.createToast(getApplicationContext(), "Bluetooth Connection Created");
        }
        else {
            toastCreator.createToast(getApplicationContext(), "No device detected");
        }
    }

    public void deactivateLock(View view) throws IOException {
        outputStream.write("2".getBytes());
        toastCreator.createToast(getApplicationContext(), "Deactivated");
        enableButton(R.id.activate);
        disableButton(R.id.deactivate);
    }

    public void activateLock(View view) throws IOException {
        outputStream.write("1".getBytes());
        toastCreator.createToast(getApplicationContext(), "Activated");
        enableButton(R.id.deactivate);
        disableButton(R.id.activate);
    }
}