package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN") == PackageManager.PERMISSION_GRANTED) {

        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Utilities.showAlert(this, "This device doesn't support Bluetooth.");
        }
        else {
            int REQUEST_ENABLE_BT = 1;
            if (!bluetoothAdapter.isEnabled()) {
//                try {
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                } catch (SecurityException e) {
//
//                }
                Utilities.showAlert(this, "Please turn on Bluetooth before proceeding.");
            }
        }
    }
}