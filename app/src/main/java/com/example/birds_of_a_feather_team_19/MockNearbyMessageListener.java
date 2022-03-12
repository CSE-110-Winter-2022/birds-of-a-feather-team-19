
package com.example.birds_of_a_feather_team_19;

import android.util.Log;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MockNearbyMessageListener extends MessageListener {
    private final MessageListener messageListener;

    public void addMessage(String message) {
        messageListener.onFound(new Message(message.getBytes()));
    }

    public MockNearbyMessageListener(MessageListener realMessageListener) {
        this.messageListener = realMessageListener;
    }
}