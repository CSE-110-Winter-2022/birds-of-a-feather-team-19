
package com.example.birds_of_a_feather_team_19;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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

    private static Set<String> messages = new TreeSet<>();

    public static void addMessage(String message) {
        messages.add(message);
    }

    public MockNearbyMessageListener(MessageListener realMessageListener, int frequency, String messageStr) {
        this.index = 0;

        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            String[] messagesArr = new String[0];
            messagesArr = messages.toArray(messagesArr);

            while (index < messages.size()) {
                String messageString = messagesArr[index++];
                Message message = new Message(messageString.getBytes(StandardCharsets.UTF_8));
                this.messageListener.onFound(message);
            }
        }, 0, frequency, TimeUnit.SECONDS);
    }
}