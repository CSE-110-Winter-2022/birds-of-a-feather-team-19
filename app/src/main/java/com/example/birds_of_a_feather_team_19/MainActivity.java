package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BoF";
    public static final int USER_ID = 1;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Utilities.showAlert(this, "This app will be unable to connect you to other users. ");
                }
            });
    private AppDatabase db;
    private Message message;
    private MessageListener messageListener;
    private Map<String, String> quarterMap = new HashMap<>();
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersLayoutManager;
    private UsersViewAdapter usersViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        quarterMap.put("fa", "fall");
        quarterMap.put("wi", "winter");
        quarterMap.put("sp", "spring");
        quarterMap.put("ss1", "summer session 1");
        quarterMap.put("ss2", "summer session 2");
        quarterMap.put("sss", "special summer session");

        db = AppDatabase.singleton(this);

        MessageListener realListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found user: " + new String(message.getContent()));
                updateDatabase(new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost user: " + new String(message.getContent()));
            }
        };
        this.message = new Message("hello".getBytes(StandardCharsets.UTF_8));

        this.messageListener = new MockNearbyMessageListener(realListener, 500, "Reloading");

        updateRecylerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBluetoothPermission();
        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Button) findViewById(R.id.startStopMainButton)).setText("Start");
    }

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth Scan to find other devices. ");
            }
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADVERTISE)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth Advertise to make your device discoverable to other devices. ");
            }
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADVERTISE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth Connect to transfer data between you to other devices. ");
            }
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
        }
    }

    public void onStartStopMainButtonClicked(View view) {
        Button button = findViewById(R.id.startStopMainButton);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");
            Nearby.getMessagesClient(this).publish(message);
            Nearby.getMessagesClient(this).subscribe(messageListener);
            updateRecylerView();
        }
        else {
            button.setText("Start");
            Nearby.getMessagesClient(this).unpublish(message);
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    private void updateDatabase(String userData) {
        userData = userData.replace('\n', ',');
        Log.d(TAG,userData);
        String[] data = userData.split(",");
        Log.d(TAG,"Updating database");
        String userName = data[0];
        String userPhotoUrl = data[4];
        int userId = db.userDao().count() + 1;

        Log.d(TAG,userName + ", " + userPhotoUrl + ", " + userId);

        User studentUser = new User(userId, userName, userPhotoUrl);
        db.userDao().insert(studentUser);

        int i = 8;
        while (i < data.length) {
            String year = data[i].toLowerCase();
            String quarter = quarterMap.get(data[i + 1].toLowerCase());
            String subject = data[i + 2].toLowerCase();
            String number = data[i + 3].toLowerCase();
            String size = data[i + 4].toLowerCase();

            Log.d(TAG,year + quarter + " " + subject + number);

            Course course = new Course(userId, year, quarter, subject, number, size);
            db.courseDao().insert(course);

            i += 4;
        }

        if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("STOP")) {
            updateRecylerView();
        }
    }

    // Logging
    private void updateRecylerView() {
        Log.d(TAG,"UPDATING RECYCLER VIEW");
        List<UserPriority> userPriorities = new ArrayList<>();
        for (Course userCourse : db.courseDao().getForUser(1)) {
            for (Course course : db.courseDao().getUsers(userCourse.getYear(), userCourse.getQuarter(), userCourse.getSubject(), userCourse.getNumber())) {
                UserPriority userPriority = new UserPriority(db.userDao().get(course.getUserId()), 1);
                if (userPriorities.contains(userPriority)) {
                    userPriorities.get(userPriorities.indexOf(userPriority))
                            .setPriority(userPriorities.get(userPriorities.indexOf(userPriority)).getPriority() + 1);
                } else {
                    userPriorities.add(userPriority);
                }
            }
        }
        userPriorities.remove(new UserPriority(db.userDao().get(1), 1));

        PriorityQueue<UserPriority> userPriorityQueue = new PriorityQueue<>(userPriorities);

        List<UserPriority> users = new ArrayList<>();
        while (!userPriorityQueue.isEmpty()) {
            users.add(userPriorityQueue.poll());
        }

        Log.d(TAG, "List of Users in priority order");
        for (UserPriority priorityUsers : users) {
            String courseString = "User: " + priorityUsers.getUser().getName() + " with priority " + priorityUsers.getPriority();
            Log.d(TAG, courseString);
        }

        usersRecyclerView = findViewById(R.id.usersMainRecyclerView);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

    public void onMockMessageMainButtonClicked(View view) {
        Intent intent = new Intent(this, MockNearbyMessageActivity.class);
        startActivity(intent);
    }
}

// Test correct ordering
class UserPriority implements Comparable<UserPriority> {
    private User user;
    private int priority;

    public UserPriority(User user, int priority) {
        this.user = user;
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(UserPriority userPriority) {
        return userPriority.getPriority() - priority;
    }

    @Override
    public boolean equals(Object o) {
        return user.equals(((UserPriority) o).getUser());
    }
}
