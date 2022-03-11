package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.Session;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BoF";
    public static Message message;
    public static String USER_ID;
    private AppDatabase db;
    private Map<String, String> quarterMap;
    private Map<String, Double> sizeMap;
    private MessageListener messageListener;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersLayoutManager;
    private Session session;
    public static String sessionName;
    private UserPriorityAssigner priorityAssigner;
    private UsersViewAdapter usersViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        SharedPreferences preferences = getSharedPreferences(TAG, MODE_PRIVATE);
        if (preferences.getString("UUID", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UUID", UUID.randomUUID().toString());
            editor.commit();
        }
        this.USER_ID = preferences.getString("UUID", null);
        Log.d(TAG, "App user ID: " + this.USER_ID);

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

        messageListener = new MockNearbyMessageListener(new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found user: " + new String(message.getContent()));
                updateDatabase(new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost user: " + new String(message.getContent()));
            }

        }, 500, "Reloading");

        priorityAssigner = new SharedClassesPriorityAssigner();

        ArrayList<CharSequence> filterList = new ArrayList<>();
        for (String filter : getResources().getStringArray(R.array.filter_type)) {
            filterList.add(filter);
        }
        for (Session session : db.sessionDao().getAll()) {
            filterList.add("Session: " + session.getName());
        }

        ((TextView) findViewById(R.id.UUIDMainTextView)).setText("UUID: " + this.USER_ID);

        Button editSaveSessionNameMainButton = findViewById(R.id.editSaveSessionNameMainButton);
        EditText sessionNameMainEditText = findViewById(R.id.sessionNameMainEditText);
        sessionNameMainEditText.setText(filterList.get(0));
        sessionNameMainEditText.setInputType(0);
        sessionNameMainEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(TextUtils.isEmpty(editable) || Arrays.asList(getResources().getStringArray(R.array.filter_type)).contains(editable))) {
                    editSaveSessionNameMainButton.setVisibility(View.VISIBLE);
                }
            }
        });

        Spinner sortSpinner = findViewById(R.id.sortMainSpinner);
        ArrayAdapter<CharSequence> sortAdapter =
                ArrayAdapter.createFromResource(this, R.array.sort_type, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        Spinner filterSpinner = findViewById(R.id.filterMainSpinner);
        ArrayAdapter<CharSequence> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterList);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("Stop")) {
            publish();
        }

        updateRecyclerView(); // Need to confirm that this goes here instead of other Lifecycle
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("Stop")) {
            Session session = new Session(Utilities.getCurrentDateTime());
            Log.d(TAG, "New session created with name: " + session.getName());
            db.sessionDao().insert(session);
            for (UserPriority userPriority : ((UsersViewAdapter) ((RecyclerView) findViewById(R.id.sessionUsersMainRecyclerView)).getAdapter()).getUserPriorities()) {
                User user = userPriority.getUser();
                user.addSessionId(session.getId());
                db.userDao().update(user);
            }

            unPublish();
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    public void onStartStopSessionMainButtonClicked(View view) {
        Button startStopSessionMainButton = findViewById(R.id.startStopSessionMainButton);

        if (startStopSessionMainButton.getText().toString().equals("Start")) {
            startStopSessionMainButton.setText("Stop");

            publish();
            Nearby.getMessagesClient(this).subscribe(messageListener);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save new session name: ")
                    .setView(R.layout.new_session_name_dialog)
                    .setPositiveButton("Save", null);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(dialogInterface -> {
                Button BUTTON_POSITIVE = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                EditText newSessionNameEditText = dialog.findViewById(R.id.newSessionNameEditText);

                BUTTON_POSITIVE.setEnabled(false);
                newSessionNameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (TextUtils.isEmpty(editable)) {
                            BUTTON_POSITIVE.setEnabled(false);
                        }
                        else {
                            BUTTON_POSITIVE.setEnabled(true);
                        }
                    }
                });
                BUTTON_POSITIVE.setOnClickListener(view1 -> {
                    String sessionName = newSessionNameEditText.getText().toString();

                    if (db.sessionDao().get(sessionName) != null) {
                        Utilities.showAlert(MainActivity.this, "Session name used. ");
                        return;
                    }

                    session = new Session(sessionName);
                    ((EditText) findViewById(R.id.sessionNameMainEditText)).setText("Session: " + session.getName());
                    db.sessionDao().insert(session);
                    for (UserPriority userPriority : ((UsersViewAdapter) ((RecyclerView) findViewById(R.id.sessionUsersMainRecyclerView)).getAdapter()).getUserPriorities()) {
                        User user = userPriority.getUser();
                        user.addSessionId(session.getId());
                        db.userDao().update(user);
                    }

                    dialog.dismiss();
                });
            });
            dialog.show();

            startStopSessionMainButton.setText("Start");

            unPublish();
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    public void onEditSaveSessionNameMainButtonClicked(View view) {
        Button button = findViewById(R.id.editSaveSessionNameMainButton);
        EditText sessionNameEditText = findViewById(R.id.sessionNameMainEditText);

        if (button.getText().toString().equals("Edit")) {
            button.setText("Save");
            sessionNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else {
            button.setText("Edit");
            session.setName(sessionNameEditText.getText().toString());
            db.sessionDao().update(session);
            sessionNameEditText.setInputType(0);
        }
    }

    public void onSortFilterMainButtonClicked(View view) {
        View sortFilterLinearLayout = findViewById(R.id.sortFilterMainLinearLayout);
        if (sortFilterLinearLayout.getVisibility() == View.GONE) {
            sortFilterLinearLayout.setVisibility(View.VISIBLE);

            Spinner filterSpinner = findViewById(R.id.filterMainSpinner);
            ArrayList<CharSequence> filterList = new ArrayList<>();
            for (String filter : getResources().getStringArray(R.array.filter_type)) {
                filterList.add(filter);
            }
            for (Session session : db.sessionDao().getAll()) {
                filterList.add("Session: " + session.getName());
            }
            ArrayAdapter<CharSequence> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterList);
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterSpinner.setAdapter(filterAdapter);
        }
        else {
            sortFilterLinearLayout.setVisibility(View.GONE);

            boolean changeSessionNameMainEditText = true;
            for (String filter : getResources().getStringArray(R.array.filter_type)) {
                if (((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString().equals(filter)) {
                    changeSessionNameMainEditText = false;
                    ((EditText) findViewById(R.id.sessionNameMainEditText)).setText(filter);
                }
            }
            if (changeSessionNameMainEditText) {
                EditText sessionNameMainEditText = findViewById(R.id.sessionNameMainEditText);
                session = db.sessionDao().get(sessionNameMainEditText.getText().toString());
                sessionNameMainEditText.setText(((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString(), TextView.BufferType.NORMAL);
            }
            updateRecyclerView();
        }
    }

    public void onMockMessageMainButtonClicked(View view) {
        startActivity(new Intent(this, MockNearbyMessageActivity.class));
    }

    private void updateDatabase(String userData) {
        Log.d(TAG, userData);
        userData = userData.replace('\n', ',');
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
            if(allWaveUser.containsKey(USER_ID)){
                user.setReceivedWave(true);
                db.userDao().update(user);
            }
            if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("STOP")) {
                updateRecyclerView();
            }
            return;
        }
        Log.d(TAG, "New user encountered");
        String userName = data[5];
        String userPhotoUrl = data[10];
        Log.d(TAG,userName + ", " + userPhotoUrl + ", " + uuid);
        User studentUser = new User(uuid, userName, userPhotoUrl);
        db.userDao().insert(studentUser);
        if(allWaveUser.containsKey(USER_ID)){
            studentUser.setReceivedWave(true);
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

            db.courseDao().insert(new Course(uuid, year, quarter, subject, number, sizeMap.get(size)));

            i += 5;
        }

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("STOP")) {
            updateRecyclerView();
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
        Log.i(TAG, "UnPublishing message. ");
        Nearby.getMessagesClient(this).unpublish(message);
    }

    private void updateRecyclerView() {
        Log.d(TAG,"UPDATING RECYCLER VIEW");
        Log.d(TAG, "Filter method: " + ((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString());
        Log.d(TAG, "Sort method: " + ((Spinner) findViewById(R.id.sortMainSpinner)).getSelectedItem().toString());

        usersRecyclerView = findViewById(R.id.sessionUsersMainRecyclerView);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(db,
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
                filter = filter.replaceFirst("Session: ", "");
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