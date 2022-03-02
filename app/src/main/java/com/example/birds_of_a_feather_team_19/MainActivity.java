package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;
    public static final int USER_ID = 1;
    private static final String TAG = "BoF";
    private MessageListener messageListener;
    private Message message;
    private Map<String, String> quarterMap = new HashMap<>();
    private UserPriorityAssigner priorityAssigner;

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

        Spinner filterSpinner = findViewById(R.id.sort_list_students_filter);
        ArrayAdapter<CharSequence> filterAdapter =
                ArrayAdapter.createFromResource(this, R.array.sort_type, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        priorityAssigner = new SharedClassesPriorityAssigner();

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBluetoothStatus();
        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Button) findViewById(R.id.startStopMainButton)).setText("Start");
    }

    public void onSetFilterClicked(View view) {
        String filterType = ((Spinner) findViewById(R.id.sort_list_students_filter)).getSelectedItem().toString().toLowerCase(Locale.ROOT);
        switch (filterType) {
            case "default":
                this.priorityAssigner = new SharedClassesPriorityAssigner();
                break;
            case "prioritize recent":
                this.priorityAssigner = new SharedRecentClassPriorityAssigner();
                break;
            case "prioritize small classes":
                this.priorityAssigner = new SharedClassSizePriorityAssigner();
                break;
            case "this quarter only":
                this.priorityAssigner = new SharedThisQuarterPriorityAssigner();
                break;
        }
    }

    // Needs testing
    private void checkBluetoothStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth to connect you to other users. ");
            }
        }
    }

    public void onStartStopMainButtonClicked(View view) {
        Button button = findViewById(R.id.startStopMainButton);
        //Intent intent = new Intent(MainActivity.this, BluetoothService.class);
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

    // Logging
    private void updateDatabase(String userData) {
//        userData = userData.toLowerCase(Locale.ROOT);
        userData = userData.replace('\n', ',');
        Log.d(TAG,userData);
        String[] data = userData.split(",");
        Log.d(TAG,"Updating database");
//        for (int i = 0; i < data.length; i++) {
//            String s = data[i];
//            System.out.println(i + ": " + s);
//        }
        String userName = data[0];
        String userPhotoUrl = data[4];
        int userId = db.userDao().count() + 1;

        Log.d(TAG,userName + ", " + userPhotoUrl + ", " + userId);

        User studentUser = new User(userId, userName, userPhotoUrl);
        db.userDao().insert(studentUser);

        int i = 8;
        while (i < data.length) {
            String year = data[i].toLowerCase(Locale.ROOT);
            String quarter = quarterMap.get(data[i + 1].toLowerCase(Locale.ROOT));
            String subject = data[i + 2].toLowerCase(Locale.ROOT);
            String number = data[i + 3].toLowerCase(Locale.ROOT);



            Log.d(TAG,year + quarter + " " + subject + number);

            Course course = new Course(userId, year, quarter, subject, number);
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
//        List<UserPriority> userPriorities = new ArrayList<>();
//        for (Course userCourse : db.courseDao().getForUser(1)) {
//            for (Course course : db.courseDao().getUsers(userCourse.getYear(), userCourse.getQuarter(), userCourse.getSubject(), userCourse.getNumber())) {
//                UserPriority userPriority = new UserPriority(db.userDao().get(course.getUserId()), 1);
//                if (userPriorities.contains(userPriority)) {
//                    userPriorities.get(userPriorities.indexOf(userPriority))
//                            .setPriority(userPriorities.get(userPriorities.indexOf(userPriority)).getPriority() + 1);
//                } else {
//                    userPriorities.add(userPriority);
//                }
//            }
//        }

        List<UserPriority> listUsers = new ArrayList<>();

        List<Course> userCourses = db.courseDao().getForUser(1);
        for (User user : db.userDao().getAll()) {
            if (user.getId() == 1)
                continue;
            List<Course> otherUserCourses = db.courseDao().getForUser(user.getId());
            double priority = 0;
            for (Course cUser : userCourses) {
                for (Course c : otherUserCourses) {
                    if (cUser.equals(c)) {
                        priority += priorityAssigner.getPriority(c);
                    }
                }
            }
            if (!(priority > 0)) {
                continue;
            }
            UserPriority userPriority = new UserPriority(user, priority);
            listUsers.add(userPriority);
        }

//        userPriorities.remove(new UserPriority(db.userDao().get(1), 1));

        PriorityQueue<UserPriority> userPriorityQueue = new PriorityQueue<>(listUsers);

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