package com.example.birds_of_a_feather_team_19;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BluetoothService extends Service {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(BluetoothService.this, "Service started", Toast.LENGTH_SHORT).show();

        executor.submit(() -> {
            synchronized (this) {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(BluetoothService.this, "Service stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}