package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.Session;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BoF";
    public static String USER_ID;
    private AppDatabase db;
    public static Message message;
    private MessageListener messageListener;
    private Map<String, String> quarterMap = new HashMap<>();
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersLayoutManager;
    private UsersViewAdapter usersViewAdapter;
    private UserPriorityAssigner priorityAssigner;
    private int currentSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");
        this.currentSessionId = 0;
        SharedPreferences preferences = getSharedPreferences(TAG, MODE_PRIVATE);
        if (preferences.getString("UUID", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            String newUUID = UUID.randomUUID().toString();
            editor.putString("UUID", newUUID);
            editor.commit();
        }
        //this.USER_ID = preferences.getString("UUID", null);
        this.USER_ID = "4b295157-ba31-4f9f-8401-5d85d9cf659a";
        Log.d(TAG, "User ID: " + USER_ID);


        quarterMap.put("fa", "fall");
        quarterMap.put("wi", "winter");
        quarterMap.put("sp", "spring");
        quarterMap.put("ss1", "summer session 1");
        quarterMap.put("ss2", "summer session 2");
        quarterMap.put("sss", "special summer session");

        Spinner filterSpinner = findViewById(R.id.sort_list_students_filter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch ((int) id) {
                    case 0:
                        priorityAssigner = new SharedClassesPriorityAssigner();
                        break;
                    case 1:
                        priorityAssigner = new SharedRecentClassPriorityAssigner();
                        break;
                    case 2:
                        priorityAssigner = new SharedClassSizePriorityAssigner();
                        break;
                    case 3:
                        priorityAssigner = new SharedThisQuarterPriorityAssigner();
                        break;
                }
                updateRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });
//        ArrayAdapter<CharSequence> filterAdapter =
//                ArrayAdapter.createFromResource(this, R.array.sort_type, android.R.layout.simple_spinner_item);
//        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        filterSpinner.setAdapter(filterAdapter);

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
        //this.message = new Message("hello".getBytes(StandardCharsets.UTF_8));

        this.messageListener = new MockNearbyMessageListener(realListener, 500, "Reloading");

        updateRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        } else {
            updateRecyclerView();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("Stop")){
            publish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (((Button) findViewById(R.id.startStopMainButton)).getText().toString() == "Stop"){
            unPublish();
        }
    }


    public void publish() {
        User me = db.userDao().get(USER_ID);
        String myName = me.getName();
        String photoURL = me.getPhotoURL();
        List<Course> myCourses = db.courseDao().getForUser(USER_ID);
        StringBuilder courses = new StringBuilder();
        for (int i = 0; i < myCourses.size(); i++) {
            courses.append(myCourses.get(i).getYear());
            courses.append(",");
            courses.append(myCourses.get(i).getQuarter());
            courses.append(",");
            courses.append(myCourses.get(i).getClass());
            courses.append(",");
            courses.append(myCourses.get(i).getNumber());
            courses.append(",");
            courses.append(myCourses.get(i).getSize());
            courses.append("\n");
        }
        String allMyCourse = courses.toString();

        String sentMessage = USER_ID + ",,,,\n" +
                myName + ",,,,\n" +
                photoURL + ",,,,\n" +
                allMyCourse;

        List<User> allUsers = db.userDao().getAll();
        for (int i = 0; i < allUsers.size(); i++) {
            User curr = allUsers.get(i);
            if(curr.isWave() == true){
                if(i > 0){
                    sentMessage = sentMessage + "\n";
                }
                sentMessage = sentMessage + curr.getId() + ",wave,,,";
            }
        }

        Log.i(TAG, "Publishing message: " + sentMessage);
        message = new Message(sentMessage.getBytes());
        Nearby.getMessagesClient(this).publish(message);
    }

    public void unPublish() {
        Log.i(TAG, "UnPublishing.");
        Nearby.getMessagesClient(this).unpublish(message);
    }


    public void onStartStopMainButtonClicked(View view) {
        Button button = findViewById(R.id.startStopMainButton);
        if (button.getText().toString().equals("Start")) {
            Session newSession = new Session(Utilities.getCurrentDateTime());
            db.sessionDao().insert(newSession);
            this.currentSessionId = newSession.getId();
            button.setText("Stop");
            publish();
            Nearby.getMessagesClient(this).subscribe(messageListener);
            updateRecyclerView();
        } else {
            button.setText("Start");
            unPublish();
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    private void updateDatabase(String userData) {

        userData = userData.replace('\n', ',');
        Log.d(TAG,userData);
        String[] data = userData.split(",");
        int length = data.length;
        Log.d(TAG,"Updating database");
        String uuid = data[0];
        Map<String, String> allWaveUser = new HashMap<>();
        for (int i = 1; i < length; i+=5) {
            if(data[i].equals("wave")){
                allWaveUser.put(data[i - 1], data[i]);
            }
        }

        if (db.userDao().get(uuid) != null) {
            User user = db.userDao().get(uuid);
            user.addSessionId(currentSessionId);
            if(allWaveUser.containsKey(USER_ID)){
                user.setReceiveWave(true);
                db.userDao().update(user);
            }
            if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("STOP")) {
                updateRecyclerView();
            }
            return;
        }
        String userName = data[5];
        String userPhotoUrl = data[10];
        Log.d(TAG,userName + ", " + userPhotoUrl + ", " + uuid);
        User studentUser = new User(uuid, userName, userPhotoUrl);
        studentUser.addSessionId(currentSessionId);
        db.userDao().insert(studentUser);
        if(allWaveUser.containsKey(USER_ID)){
            studentUser.setReceiveWave(true);
            db.userDao().update(studentUser);
        }
        int i = 15;
        while (i < data.length) {
            String year = data[i].toLowerCase();
            String quarter = quarterMap.get(data[i + 1].toLowerCase());
            String subject = data[i + 2].toLowerCase();
            String number = data[i + 3].toLowerCase();
            String size = data[i + 4].toLowerCase();

            Log.d(TAG,year + quarter + " " + subject + number + " " + size);

            db.courseDao().insert(new Course(uuid, year, quarter, subject, number, size));

            i += 5;
        }

        if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("STOP")) {
            updateRecyclerView();
        }
    }

    private void updateRecyclerView() {
        Log.d(TAG,"UPDATING RECYCLER VIEW");

        List<UserPriority> listUsers = new ArrayList<>();

        List<Course> userCourses = db.courseDao().getForUser(USER_ID);
        for (User user : db.userDao().getAll()) {
            if (user.getId().equals(USER_ID) || !user.getSessionIds().contains(currentSessionId))
                continue;
            List<Course> otherUserCourses = db.courseDao().getForUser(user.getId());
            double priority = 0;
            int sharedClasses = 0;
            for (Course cUser : userCourses) {
                for (Course c : otherUserCourses) {
                    if (cUser.equals(c)) {
                        priority += priorityAssigner.getPriority(c);
                        sharedClasses++;
                    }
                }
            }
            if (!(priority > 0)) {
                continue;
            }
            UserPriority userPriority = new UserPriority(user, priority, sharedClasses);
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