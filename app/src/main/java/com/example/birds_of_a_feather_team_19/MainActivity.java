package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        db = AppDatabase.singleton(this);

        /*MessageListener realListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found user: " + new String(message.getContent()));
                updateDatabase();
                updateRecylerView();
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost user: " + new String(message.getContent()));
            }
        };
        this.messageListener = new MockNearbyMessageListener(realListener, 5, "Reloading");
*/
        updateRecylerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Nearby.getMessagesClient(this).publish(message);
        //Nearby.getMessagesClient(this).subscribe(messageListener);
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
    /*private void checkBluetoothStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth to connect you to other users. ");
            }
        }
    }*/

    public void onStartStopMainClicked(View view) {
        Button button = findViewById(R.id.buttonStartStopMain);
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");
            //loadUsers();
          
            List<User> users = db.userDao().getAll();
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

    @Override
    protected void onStop() {
        super.onStop();

        //Nearby.getMessagesClient(this).unpublish(message);
        //Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    private void updateDatabase() {
    }

    private void updateRecylerView() {
        List<UserPriority> userPriorities = new ArrayList<>();
        for (Course userCourse : db.courseDao().getForUser(1)) {
            for (Course course : db.courseDao().getUsers(userCourse.getYear(), userCourse.getTerm(), userCourse.getSubject(), userCourse.getNumber())) {
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
