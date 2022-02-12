package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;
    private static final String TAG = "BoF";
    private MockNearbyMessageListener mockNearbyMessageListener;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        db = AppDatabase.singleton(this);

        MessageListener realListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String user = new String(message.getContent());
                Log.d(TAG, "Found user: " + user);
                updateDatabase(user);
                updateRecylerView();
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost user: " + new String(message.getContent()));
            }
        };

        message = new Message(("").getBytes(StandardCharsets.UTF_8));
        mockNearbyMessageListener = new MockNearbyMessageListener(realListener, 3, message.toString());

        updateRecylerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //checkBluetoothStatus();

        if (db.userDao().get(1) == null) {
            Intent intent = new Intent(this, AddNameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Nearby.getMessagesClient(this).unpublish(message);
    }

    /*private void checkBluetoothStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth to connect you to other users. ");
            }
        }
    }*/

    public void onStartStopMainButtonClicked(View view) {
        Button button = findViewById(R.id.startStopMainButton);
        //Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");
            Nearby.getMessagesClient(this).subscribe(mockNearbyMessageListener);
        }
        else {
            button.setText("Start");
            Nearby.getMessagesClient(this).unsubscribe(mockNearbyMessageListener);
        }
    }

    private void updateDatabase(String user) {
        String userName = user.substring(0, user.indexOf("\n"));
        if (db.userDao().get(userName) != null) {
            Utilities.showAlert(this, "Username taken. Please enter another username");
            return;
        }
        user = user.substring(user.indexOf("\n") + 1);
        String userPhotoURL = user.substring(0, user.indexOf("\n"));
        if (photoURLInvalid(userPhotoURL)) {
            Utilities.showAlert(this, "Please enter a valid photo");
            return;
        }
        user = user.substring(user.indexOf("\n") + 1);
        db.userDao().insert(new User(userName, userPhotoURL));

        user = user.toLowerCase() + "\n";
        String courseYear, courseQuarter, courseSubject, courseNumber;
        while (!user.isEmpty()) {
            try {
                courseYear = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseQuarter = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseSubject = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseNumber = user.substring(0, user.indexOf("\n"));
                user = user.substring(user.indexOf("\n") + 1);
            } catch (IndexOutOfBoundsException e) {
                Utilities.showAlert(this, "Please enter valid courses");
                return;
            }

            switch (courseQuarter) {
                case "fa":
                    courseQuarter = "fall";
                    break;
                case "wi":
                    courseQuarter = "winter";
                    break;
                case "sp":
                    courseQuarter = "spring";
                    break;
                case "ss1":
                    courseQuarter = "summer session 1";
                    break;
                case "ss2":
                    courseQuarter = "summer session 2";
                    break;
                case "sss":
                    courseQuarter = "special summer session";
                    break;
                default:
                    Utilities.showAlert(this, "Please enter valid courses");
                    return;
            }
            db.courseDao().insert(new Course(db.userDao().get(userName).getId(), courseYear, courseQuarter, courseSubject, courseNumber));

            message = null;
        }
    }
    private boolean photoURLInvalid(String photoURL) {
        ExecutorService imageExecutor = Executors.newSingleThreadExecutor();

        if (!photoURL.equals("")) {
            Future<Boolean> future = (imageExecutor.submit(() -> BitmapFactory.decodeStream(new URL(photoURL).openStream()) == null));
            try {
                return future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
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
        userPriorities.remove(new UserPriority(db.userDao().get(1), 1));

        List<UserPriority> users = new ArrayList<>();
        for (UserPriority userPriority : userPriorities) {
            users.add(userPriority);
        }

        usersRecyclerView = findViewById(R.id.usersMainRecyclerView);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

    public void onMockMessageMainButtonClicked(View view) {
        setContentView(R.layout.activity_mock_nearby_message);
    }

    public void onEnterMockNearbyMessageButtonClicked(View view) {
        TextView userDetail = findViewById(R.id.userDetailMockNearbyMessageEditText);
        String user = userDetail.getText().toString();
        String userName = user.substring(0, user.indexOf("\n"));
        if (db.userDao().get(userName) != null) {
            Utilities.showAlert(this, "Username taken. Please enter another username");
            return;
        }
        user = user.substring(user.indexOf("\n") + 1);
        String userPhotoURL = user.substring(0, user.indexOf("\n"));
        if (photoURLInvalid(userPhotoURL)) {
            Utilities.showAlert(this, "Please enter a valid photo");
            return;
        }
        user = user.substring(user.indexOf("\n") + 1);
        db.userDao().insert(new User(userName, userPhotoURL));

        user = user.toLowerCase() + "\n";
        String courseYear, courseQuarter, courseSubject, courseNumber;
        while (!user.isEmpty()) {
            try {
                courseYear = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseQuarter = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseSubject = user.substring(0, user.indexOf(","));
                user = user.substring(user.indexOf(",") + 1);
                courseNumber = user.substring(0, user.indexOf("\n"));
                user = user.substring(user.indexOf("\n") + 1);
            } catch (IndexOutOfBoundsException e) {
                Utilities.showAlert(this, "Please enter valid courses");
                return;
            }

            switch (courseQuarter) {
                case "fa":
                    courseQuarter = "fall";
                    break;
                case "wi":
                    courseQuarter = "winter";
                    break;
                case "sp":
                    courseQuarter = "spring";
                    break;
                case "ss1":
                    courseQuarter = "summer session 1";
                    break;
                case "ss2":
                    courseQuarter = "summer session 2";
                    break;
                case "sss":
                    courseQuarter = "special summer session";
                    break;
                default:
                    Utilities.showAlert(this, "Please enter valid courses");
                    return;
            }
            db.courseDao().insert(new Course(db.userDao().get(userName).getId(), courseYear, courseQuarter, courseSubject, courseNumber));
        }

        message = new Message(userDetail.getText().toString().getBytes(StandardCharsets.UTF_8));

        userDetail.setText("");
    }

    public void onBackMockNearbyMessageButtonClicked(View view) {
        setContentView(R.layout.activity_main);
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
