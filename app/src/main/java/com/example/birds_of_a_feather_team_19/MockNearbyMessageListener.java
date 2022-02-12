package com.example.birds_of_a_feather_team_19;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MockNearbyMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    public MockNearbyMessageListener(MessageListener realMessageListener, int frequency, String messageStr) {
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
            this.messageListener.onFound(message);
        }, 0, frequency, TimeUnit.SECONDS);
    }
}
