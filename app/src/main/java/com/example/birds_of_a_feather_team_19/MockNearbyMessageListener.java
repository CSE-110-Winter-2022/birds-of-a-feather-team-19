
package com.example.birds_of_a_feather_team_19;

import android.util.Log;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MockNearbyMessageListener extends MessageListener implements Serializable {
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    private int index;

    private static List<String> messages = new ArrayList<>();

    public static void addMessage(String message) {
        if (messages.indexOf(message) < 0)
            messages.add(message);
    }

    public MockNearbyMessageListener(MessageListener realMessageListener, int frequency, String messageStr) {
        this.index = 0;

        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            while (index < messages.size()) {
                String messageString = messages.get(index++);
                Log.d("BoF", messageString);
                Message message = new Message(messageString.getBytes(StandardCharsets.UTF_8));
                this.messageListener.onFound(message);
            }
        }, 0, frequency, TimeUnit.SECONDS);
    }
}