package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.Session;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BoF";
    public static String USER_ID;
    private AppDatabase db;
    private Message message;
    private MessageListener messageListener;
    private Map<String, String> quarterMap;
    private Map<String, Double> sizeMap;
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

        db = AppDatabase.singleton(this);

        quarterMap = new HashMap<>();
        quarterMap.put("fa", "fall");
        quarterMap.put("wi", "winter");
        quarterMap.put("sp", "spring");
        quarterMap.put("ss1", "summer session 1");
        quarterMap.put("ss2", "summer session 2");
        quarterMap.put("sss", "special summer session");

        sizeMap = new HashMap<>();
        sizeMap.put("tiny", 1.00);
        sizeMap.put("small", 0.33);
        sizeMap.put("medium", 0.18);
        sizeMap.put("large", 0.10);
        sizeMap.put("huge", 0.06);
        sizeMap.put("gigantic", 0.03);

        this.currentSessionId = 0;
        SharedPreferences preferences = getSharedPreferences(TAG, MODE_PRIVATE);
        if (preferences.getString("UUID", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            String newUUID = UUID.randomUUID().toString();
            editor.putString("UUID", newUUID);
            editor.commit();
        }
        this.USER_ID = preferences.getString("UUID", null);
        Log.d(TAG, "User ID: " + USER_ID);

        Spinner sortSpinner = findViewById(R.id.sortMainSpinner);
        ArrayAdapter<CharSequence> sortAdapter =
                ArrayAdapter.createFromResource(this, R.array.sort_type, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        Spinner filterSpinner = findViewById(R.id.filterMainSpinner);
        ArrayList<String> filterList = new ArrayList<>();
        for (String filter : getResources().getStringArray(R.array.filter_type)) {
            filterList.add(filter);
        }
        for (Session session : db.sessionDao().getAll()) {
            filterList.add(session.getSessionName());
        }
        ArrayAdapter<CharSequence> filterAdapter =
                ArrayAdapter.createFromResource(this, R.array.filter_type, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        /* Spinner filterSpinner = findViewById(R.id.sort_list_students_filter);

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
//        filterSpinner.setAdapter(filterAdapter); */

        priorityAssigner = new SharedClassesPriorityAssigner();

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

    public void onStartStopMainButtonClicked(View view) {
        Button button = findViewById(R.id.startStopMainButton);
        if (button.getText().toString().equals("Start")) {
            Session newSession = new Session(Utilities.getCurrentDateTime());
            db.sessionDao().insert(newSession);
            this.currentSessionId = newSession.getId();
            Log.d(TAG, "New session created with ID " + currentSessionId);
            button.setText("Stop");
            Nearby.getMessagesClient(this).publish(message);
            Nearby.getMessagesClient(this).subscribe(messageListener);
        } else {
            button.setText("Start");
            Nearby.getMessagesClient(this).unpublish(message);
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    public void onSortFilterMainButtonClicked(View view) {
        View sortFilterLinearLayout = findViewById(R.id.sortFilterMainLinearLayout);
        RecyclerView usersRecyclerView = findViewById(R.id.usersMainRecyclerView);
        if (sortFilterLinearLayout.getVisibility() == View.GONE) {
            usersRecyclerView.setVisibility(View.GONE);
            sortFilterLinearLayout.setVisibility(View.VISIBLE);
        }
        else {
            usersRecyclerView.setVisibility(View.VISIBLE);
            sortFilterLinearLayout.setVisibility(View.GONE);
        }
    }

    public void onMockMessageMainButtonClicked(View view) {
        startActivity(new Intent(this, MockNearbyMessageActivity.class));
    }

    private void updateDatabase(String userData) {
        userData = userData.replace('\n', ',');
        Log.d(TAG,userData);
        String[] data = userData.split(",");
        Log.d(TAG,"Updating database");
        String uuid = data[0];
        if (db.userDao().get(uuid) != null) {
            User user = db.userDao().get(uuid);
            user.addSessionId(currentSessionId);
            if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("STOP")) {
                updateRecyclerView();
            }
            return;
        }
        Log.d(TAG, "New user encountered");
        String userName = data[5];
        String userPhotoUrl = data[10];

        Log.d(TAG,userName + ", " + userPhotoUrl + ", " + uuid);

        User studentUser = new User(uuid, userName, userPhotoUrl);
        studentUser.addSessionId(currentSessionId);
        db.userDao().insert(studentUser);

        int i = 15;
        while (i < data.length) {
            String year = data[i].toLowerCase();
            String quarter = quarterMap.get(data[i + 1].toLowerCase());
            String subject = data[i + 2].toLowerCase();
            String number = data[i + 3].toLowerCase();
            String size = data[i + 4].toLowerCase();

            Log.d(TAG,year + quarter + " " + subject + number + " " + size);

            db.courseDao().insert(new Course(uuid, year, quarter, subject, number, sizeMap.get(size)));

            i += 5;
        }

        if (((Button) findViewById(R.id.startStopMainButton)).getText().toString().equals("STOP")) {
            updateRecyclerView();
        }
    }

    private void updateRecyclerView() {
        Log.d(TAG,"UPDATING RECYCLER VIEW");

        /* List<Course> userCourses = db.courseDao().getForUser(USER_ID);
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
        } */



        Log.d(TAG, "Filter method: " + ((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString());
        Log.d(TAG, "Sort method: " + ((Spinner) findViewById(R.id.sortMainSpinner)).getSelectedItem().toString());


        usersRecyclerView = findViewById(R.id.usersMainRecyclerView);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(
                sortUsers(filterUsers(((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString()),
                ((Spinner) findViewById(R.id.sortMainSpinner)).getSelectedItem().toString()));
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

    private List<User> filterUsers(String filter) {
        switch (filter) {
            case "None":
                List<User> users = db.userDao().getAll();
                users.remove(new User(getSharedPreferences(TAG, MODE_PRIVATE).getString("UUID", null), "", ""));
                return users;
            case "Favorite":
                return db.userDao().getFavorite(true);
            default:
                return db.sessionDao().getUsersInSession(db.sessionDao().get(filter).getId());
        }
    }

    private List<UserPriority> sortUsers(List<User> users, String sort) {
        List<UserPriority> usersPriorities = new ArrayList<>();
        PriorityQueue<UserPriority> userPrioritiesPQ = new PriorityQueue<>();
        for (User user : users) {
            double priority = 0;
            int sharedClasses = getSharedClasses(user);
            switch (sort) {
                case "Course Recency":
                    for (Course course : db.courseDao().getForUser(user.getId())) {
                        String[] currentQuarterYear = Utilities.getCurrentQuarterYear();
                        int age = Utilities.getCourseAge(course.getYear(), course.getQuarter(), currentQuarterYear[1], currentQuarterYear[2]);
                        age = 5 - age;
                        if (age < 1) {
                            age = 1;
                        }
                        priority += age;
                    }
                case "Course Size":
                    for (Course course : db.courseDao().getForUser(user.getId())) {
                        priority += course.getSize();
                    }
                default:
                    priority = sharedClasses;
            }
            userPrioritiesPQ.add(new UserPriority(user, priority, sharedClasses));
        }
        while (!userPrioritiesPQ.isEmpty()) {
            usersPriorities.add(userPrioritiesPQ.poll());
        }
        return usersPriorities;
    }
    private int getSharedClasses(User user) {
        int sharedClasses = 0;
        for (Course course : db.courseDao().getForUser(getSharedPreferences(TAG, MODE_PRIVATE).getString("UUID", null))) {
            for (Course userCourse : db.courseDao().getForUser(user.getId())) {
                if (course.equals(userCourse)) {
                    sharedClasses++;
                }
            }
        }

        return sharedClasses;
    }
}