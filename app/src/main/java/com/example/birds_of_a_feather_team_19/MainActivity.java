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
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;
    private static final String TAG = "BoF";
    private MessageListener messageListener;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkBluetoothStatus();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        db = AppDatabase.singleton(this);

        MessageListener realListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String userData = new String(message.getContent());
//                Log.d(TAG, "Found user: " + new String(message.getContent()));
                updateDatabase(userData);
                updateRecylerView();
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost user: " + new String(message.getContent()));
            }
        };

        message = new Message("hello".getBytes(StandardCharsets.UTF_8));

        this.messageListener = new MockNearbyMessageListener(realListener, 5, "Data");



        updateRecylerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Nearby.getMessagesClient(this).publish(message);
        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("Resumed");

        if (db.userDao().get(1) == null) {
            Intent intent = new Intent(this, AddNameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Nearby.getMessagesClient(this).unpublish(message);
        Nearby.getMessagesClient(this).unsubscribe(messageListener);


    }

    private void checkBluetoothStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth to connect you to other users. ");
            }
        }
    }


    public void onAddMockStudentDataClicked(View view) {
        Intent intent = new Intent(MainActivity.this, MockNearbyMessageActivity.class);
//        intent.putExtra("messageListener", (Parcelable) this.messageListener);

        startActivity(intent);
    }

    // Remove in merge
    public void onStartStopMainClicked(View view) {
        Button button = findViewById(R.id.buttonStartStopMain);
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");
            //loadUsers();
          
            List<User> users = db.userDao().getAll();
            users.remove(0);
            usersRecyclerView = findViewById(R.id.recyclerViewUsersMain);
            usersLayoutManager = new LinearLayoutManager(this);
            usersRecyclerView.setLayoutManager(usersLayoutManager);
            usersViewAdapter = new UsersViewAdapter(users);
            usersRecyclerView.setAdapter(usersViewAdapter);
        }
        else {
            button.setText("Start");
            //stopService(intent);
        }
    }

    private void updateDatabase(String userData) {
        userData = userData.replace('\n', ',');
        System.out.println(userData);
        String[] data = userData.split(",");
        System.out.println("Updating database");
//        for (int i = 0; i < data.length; i++) {
//            String s = data[i];
//            System.out.println(i + ": " + s);
//        }
        String userName = data[0];
        String userPhotoUrl = data[5];
        int userId = db.userDao().count() + 1;

        System.out.println(userName + ", " + userPhotoUrl + ", " + userId);

        User studentUser = new User(userId, userName, userPhotoUrl);
        db.userDao().insert(studentUser);

        int i = 10;
        while (i < data.length) {
            String year = data[i];
            String quarter = data[i + 1];
            String subject = data[i + 2];
            String number = data[i + 3];

            System.out.println(year + quarter + " " + subject + number);

            Course course = new Course(userId, year, quarter, subject, number);
            db.courseDao().insert(course);

            i += 5;
        }
    }

    private void updateRecylerView() {
        List<UserPriority> userPriorities = new ArrayList<>();
        for (Course userCourse : db.courseDao().getForUser(1)) {
            for (Course course : db.courseDao().getUsers(userCourse.getYear(), userCourse.getQuarter(), userCourse.getSubject(), userCourse.getNumber())) {
                UserPriority userPriority = new UserPriority(db.userDao().get(course.getUserId()), 1);
                if (userPriorities.contains(userPriority)) {
                    userPriorities.get(userPriorities.indexOf(userPriority))
                            .setPriority(userPriorities.get(userPriorities.indexOf(userPriority)).getPriority() + 1);
                }
                else {
                    userPriorities.add(userPriority);
                }
            }
        }
        userPriorities.remove(new UserPriority(new User(1, "", ""), 1));

        List<User> users = new ArrayList<>();
        for (UserPriority userPriority : new TreeSet<>(userPriorities)) {
            users.add(userPriority.getUser());
        }

        usersRecyclerView = findViewById(R.id.recyclerViewUsersMain);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

}

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
