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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static Message message;
    public static Message newMessage;
    public static String USER_ID;
    private AppDatabase db;
    private Map<Integer, String> quarterMap;
    private Map<String, Integer> quarterMockingMap;
    private Map<String, Double> sizeMockingMap;
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

        SharedPreferences preferences = getSharedPreferences(getString(R.string.TAG), MODE_PRIVATE);
        if (preferences.getString("UUID", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UUID", UUID.randomUUID().toString());
            editor.apply();
        }
        this.USER_ID = preferences.getString("UUID", null);
        Log.d(getString(R.string.TAG), "App user ID: " + this.USER_ID);

        db = AppDatabase.singleton(this);

        quarterMap = new HashMap<>();
        quarterMap.put(1, "Winter");
        quarterMap.put(3, "Spring");
        quarterMap.put(6, "Summer Session 1");
        quarterMap.put(8, "Summer Session 2");
        quarterMap.put(6, "Special Summer Session");
        quarterMap.put(9, "Fall");
        quarterMockingMap = new HashMap<>();
        quarterMockingMap.put("WI", 1);
        quarterMockingMap.put("SP", 3);
        quarterMockingMap.put("SS1", 6);
        quarterMockingMap.put("SS2", 8);
        quarterMockingMap.put("SSS", 6);
        quarterMockingMap.put("FA", 9);
        sizeMockingMap = new HashMap<>();
        sizeMockingMap.put("Tiny", 1.00);
        sizeMockingMap.put("Small", 0.33);
        sizeMockingMap.put("Medium", 0.18);
        sizeMockingMap.put("Large", 0.10);
        sizeMockingMap.put("Huge", 0.06);
        sizeMockingMap.put("Gigantic", 0.03);

        messageListener = new MockNearbyMessageListener(new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Found user: " + new String(message.getContent()));
                updateDatabase(new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Lost user: " + new String(message.getContent()));
            }

        }, 100);

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
            Nearby.getMessagesClient(this).subscribe(messageListener);
        }

        updateRecyclerView(); // Need to confirm that this goes here instead of other Lifecycle
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        }
        else {
            User thisUser = db.userDao().get(USER_ID);
            String message = thisUser.getId() + ",,,,\n";
            message += thisUser.getName() + ",,,,\n";
            message += thisUser.getPhotoURL() + ",,,,\n";
            for (Course course : db.courseDao().getForUser(thisUser.getId())) {
                message += course.getYear() + "," + course.getQuarter() + "," + course.getSubject() + "," + course.getNumber() + "," + course.getSize() + "\n";
            }
            for (User user : db.userDao().getAll()) {
                if (user.isWave()) {
                    message += user.getId() + ",wave,,,\n";
                }
            }
            String newMessage = "";

            this.message = new Message(message.getBytes());
            this.newMessage = new Message(newMessage.getBytes());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

/*
        unPublish();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("Stop")) {
            Session session = new Session(Utilities.getCurrentDateTime());
            Log.d(getString(R.string.TAG), "New session created with name: " + session.getName());
            db.sessionDao().insert(session);
            for (UserPriority userPriority : ((UsersViewAdapter) ((RecyclerView) findViewById(R.id.sessionUsersMainRecyclerView)).getAdapter()).getUserPriorities()) {
                User user = userPriority.getUser();
                user.addSessionId(session.getId());
                db.userDao().update(user);
            }
        }

        unPublish();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
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
        Log.d(getString(R.string.TAG), "New user encountered");
        Log.d(getString(R.string.TAG), userData);
        String[] dataLines = userData.split("\n");
        List<List<String>> data = new ArrayList<>();
        for (String line : dataLines) {
            data.add(Arrays.asList(line.split(",")));
        }

        User user = db.userDao().get(data.get(0).get(0)) == null ? new User(data.get(0).get(0), data.get(1).get(0), data.get(2).get(0)) : db.userDao().get(data.get(0).get(0));
        List<Course> courses = new ArrayList<>();
        for (int i = 3; i < data.size(); i++) {
            List<String> entry = data.get(i);
            if (entry.get(1).equals("wave")) {
                if (data.get(i).get(0).equals(USER_ID)) {
                    user.setReceivedWave(true);
                }
            }
            else {
                courses.add(new Course(user.getId(),
                        Integer.parseInt(entry.get(0)),
                        quarterMockingMap.get(entry.get(1)),
                        entry.get(2).toUpperCase(Locale.ENGLISH),
                        entry.get(3).toUpperCase(Locale.ENGLISH),
                        sizeMockingMap.get(entry.get(4))));
            }
        }

        if (db.userDao().get(user.getId()) == null) {
            db.userDao().insert(user);
        }
        else {
            db.userDao().update(user);
        }
        for (Course course : courses) {
            if (!db.courseDao().getForUser(user.getId()).contains(course)) {
                db.courseDao().insert(course);
            }
        }

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("STOP")) {
            updateRecyclerView();
        }
    }

    public void publish() {
        String newMessage = new String(this.newMessage.getContent());

        if (newMessage.isEmpty()) {
            return;
        }

        unPublish();
        String message = new String(this.message.getContent()) + newMessage;
        newMessage = "";
        Log.i(getString(R.string.TAG), "Publishing message: " + message);
        this.message = new Message(message.getBytes());
        this.newMessage = new Message(newMessage.getBytes());
        Nearby.getMessagesClient(this).publish(this.message);
    }
    public void unPublish() {
        Log.i(getString(R.string.TAG), "UnPublishing message. ");
        Nearby.getMessagesClient(this).unpublish(message);
    }

    private void updateRecyclerView() {
        Log.d(getString(R.string.TAG),"UPDATING RECYCLER VIEW");
        Log.d(getString(R.string.TAG), "Filter method: " + ((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString());
        Log.d(getString(R.string.TAG), "Sort method: " + ((Spinner) findViewById(R.id.sortMainSpinner)).getSelectedItem().toString());

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
                users.remove(new User(getSharedPreferences(getString(R.string.TAG), MODE_PRIVATE).getString("UUID", null), "", ""));
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
                        int age = Utilities.getCourseAge(Integer.toString(course.getYear()), quarterMap.get(course.getQuarter()).toLowerCase(), currentQuarterYear[1], currentQuarterYear[2]);
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
        List<UserPriority> wavedUsers = new ArrayList<>();
        for (int i = 0; i < usersPriorities.size(); i++) {
            UserPriority userPriority = usersPriorities.get(i);
            if (userPriority.getUser().isWave()) {
                wavedUsers.add(userPriority);
                usersPriorities.remove(i);
            }
        }
        usersPriorities.addAll(0, wavedUsers);
        return usersPriorities;
    }

    private int getSharedClasses(User user) {
        int sharedClasses = 0;
        for (Course course : db.courseDao().getForUser(getSharedPreferences(getString(R.string.TAG), MODE_PRIVATE).getString("UUID", null))) {
            for (Course userCourse : db.courseDao().getForUser(user.getId())) {
                if (course.equals(userCourse)) {
                    sharedClasses++;
                }
            }
        }

        return sharedClasses;
    }
}