
package com.example.birds_of_a_feather_team_19;

import android.util.Log;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MockNearbyMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;
    private ScheduledFuture future;

    private static List<String> messages = new ArrayList<>();
    private int index;

    public static void addMessage(String message) {
        if (messages.indexOf(message) < 0)
            messages.add(message);
    }

    public MockNearbyMessageListener(MessageListener realMessageListener) {
        this.index = 0;

        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        future = executor.scheduleAtFixedRate(() -> {
            while (index < messages.size()) {
                String messageString = messages.get(index++);
                Log.d("BoF", messageString);

                Message message = new Message(messageString.getBytes());
                this.messageListener.onFound(message);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        future.cancel(true);
    }
}