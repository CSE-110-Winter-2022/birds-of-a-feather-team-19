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
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static Message message;
    public static Message newMessage;
    public String USER_ID;
    private AppDatabase db;
    private Map<Integer, String> quarterMap;
    private Map<String, Integer> quarterMockingMap;
    private Map<String, Double> sizeMockingMap;
    private Map<Integer, String> quarterCodeMap;
    private Map<Double, String> classSizeMap;
    private MessageListener messageListener;
    private MockNearbyMessageListener mockMessageListener;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersLayoutManager;
    private Session session;
    private String sessionName;
    private int currentSessionId;
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
        this.currentSessionId = -1;

        db = AppDatabase.singleton(this);

        // TODO: Change key value
        quarterMap = new HashMap<>();
        quarterMap.put(1, "Winter");
        quarterMap.put(3, "Spring");
        quarterMap.put(6, "Summer Session 1");
        quarterMap.put(8, "Summer Session 2");
        quarterMap.put(7, "Special Summer Session");
        quarterMap.put(9, "Fall");

        // TODO: Change key value
        quarterMockingMap = new HashMap<>();
        quarterMockingMap.put("WI", 1);
        quarterMockingMap.put("SP", 3);
        quarterMockingMap.put("SS1", 6);
        quarterMockingMap.put("SS2", 8);
        quarterMockingMap.put("SSS", 7);
        quarterMockingMap.put("FA", 9);
        quarterCodeMap = new HashMap<>();
        quarterCodeMap.put(1, "WI");
        quarterCodeMap.put(3, "SP");
        quarterCodeMap.put(6, "SS1");
        quarterCodeMap.put(8, "SS2");
        quarterCodeMap.put(7, "SSS");
        quarterCodeMap.put(9, "FA");
        classSizeMap = new HashMap<>();
        classSizeMap.put(1.00, "Tiny");
        classSizeMap.put(0.33, "Small");
        classSizeMap.put(0.18, "Medium");
        classSizeMap.put(0.10, "Large");
        classSizeMap.put(0.06, "Huge");
        classSizeMap.put(0.03, "Gigantic");
        sizeMockingMap = new HashMap<>();
        sizeMockingMap.put("Tiny", 1.00);
        sizeMockingMap.put("Small", 0.33);
        sizeMockingMap.put("Medium", 0.18);
        sizeMockingMap.put("Large", 0.10);
        sizeMockingMap.put("Huge", 0.06);
        sizeMockingMap.put("Gigantic", 0.03);

        mockMessageListener = new MockNearbyMessageListener(new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Found user: " + new String(message.getContent()));
                updateDatabase(new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Lost user: " + new String(message.getContent()));
            }

        });
        messageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Message found");
                updateDatabase(new String(message.getContent()));
            }

            @Override
            public void onLost(@NonNull Message message) {
                Log.d(getString(R.string.TAG), "Lost user: " + new String(message.getContent()));
            }
        };

        priorityAssigner = new SharedClassesPriorityAssigner();

        ArrayList<CharSequence> filterList = new ArrayList<>();
        for (String filter : getResources().getStringArray(R.array.filter_type)) {
            filterList.add(filter);
        }
        for (Session session : db.sessionDao().getAll()) {
            filterList.add("Session: " + session.getName());
        }

        this.currentSessionId = 0;

        ((TextView) findViewById(R.id.UUIDMainTextView)).setText("UUID: " + this.USER_ID);

        EditText sessionNameEditText = findViewById(R.id.sessionNameMainEditText);
        Button editSaveSessionNameButton = findViewById(R.id.editSaveSessionNameMainButton);
        sessionNameEditText.setText(filterList.get(0));
        sessionNameEditText.setInputType(0);
        sessionNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(TextUtils.isEmpty(editable) || Arrays.asList(getResources().getStringArray(R.array.filter_type)).contains(editable))) {
                    editSaveSessionNameButton.setVisibility(View.VISIBLE);
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

        updateRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("Stop")) {
            mockMessageListener.start();
            publish();
            Nearby.getMessagesClient(this).subscribe(messageListener);
        }

        //updateRecyclerView(); // Need to confirm that this goes here instead of other Lifecycle
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db.userDao().get(USER_ID) == null) {
            startActivity(new Intent(this, AddNameActivity.class));
        }
        else {
            User thisUser = db.userDao().get(USER_ID);
            String message = thisUser.getId() + ",,,,\n"
                    + thisUser.getName() + ",,,,\n"
                    + thisUser.getPhotoURL() + ",,,,\n";
            for (Course course : db.courseDao().getForUser(thisUser.getId())) {
                message += course.getYear() + "," + quarterCodeMap.get(course.getQuarter()) + ","
                        + course.getSubject() + "," + course.getNumber() + ","
                        + classSizeMap.get(course.getSize()) + "\n";
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

        if (((Button) findViewById(R.id.startStopSessionMainButton)).getText().toString().equals("Stop")) {
            Log.d(getString(R.string.TAG), "New session " + session.getId() + " saved with default name: " + session.getName());
//            db.sessionDao().insert(session);
//            for (UserPriority userPriority : ((UsersViewAdapter) ((RecyclerView) findViewById(R.id.sessionUsersMainRecyclerView)).getAdapter()).getUserPriorities()) {
//                User user = userPriority.getUser();
//                user.addSessionId(session.getId());
//                db.userDao().update(user);
//            }
            mockMessageListener.stop();
            unPublish();
            Nearby.getMessagesClient(this).unsubscribe(messageListener);
        }
    }

    public void onStartStopSessionMainButtonClicked(View view) {
        Button startStopSessionButton = findViewById(R.id.startStopSessionMainButton);

        if (startStopSessionButton.getText().toString().equals("Start")) {
            String defaultName = Utilities.getCurrentDateTime();
            db.sessionDao().insert(new Session(defaultName));
            this.session = db.sessionDao().get(defaultName);
            Log.d(getString(R.string.TAG), "New session " + session.getId() + " saved with default name: " + session.getName());

            mockMessageListener.start();
            publish();
            Nearby.getMessagesClient(this).subscribe(messageListener);

            ((EditText) findViewById(R.id.sessionNameMainEditText)).setText(defaultName, TextView.BufferType.NORMAL);
            startStopSessionButton.setText("Stop");
            findViewById(R.id.mockMessageMainButton).setVisibility(View.VISIBLE);
            ((Spinner) findViewById(R.id.sortMainSpinner)).setSelection(0);
            updateFilterSpinner(defaultName);
            findViewById(R.id.filterMainSpinner).setEnabled(true);
            updateRecyclerView();
        }
        else {
            mockMessageListener.stop();
            unPublish();
            Nearby.getMessagesClient(this).unsubscribe(messageListener);

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

                    this.session.setName(sessionName);
                    Log.d(getString(R.string.TAG), "Current session ID: " + currentSessionId);
                    Log.d(getString(R.string.TAG), "Session ID updating: " + this.session.getId());
                    Log.d(getString(R.string.TAG), "Name in db: " + db.sessionDao().get(currentSessionId).getName());
                    db.sessionDao().update(this.session);
                    Log.d(getString(R.string.TAG), "Name in db: " + db.sessionDao().get(currentSessionId).getName());
                    ((EditText) findViewById(R.id.sessionNameMainEditText)).setText("Session: " + this.session.getName());

                    dialog.dismiss();
                });
            });
            dialog.show();

            startStopSessionButton.setText("Start");
            findViewById(R.id.filterMainSpinner).setEnabled(false);
            findViewById(R.id.mockMessageMainButton).setVisibility(View.INVISIBLE);
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
            session.setName(sessionNameEditText.getText().toString().replaceFirst("Session: ", ""));
            updateFilterSpinner(sessionNameEditText.getText().toString().replaceFirst("Session: ", ""));
            db.sessionDao().update(session);
            /*Session updatedSession = db.sessionDao().get(currentSessionId);
            updatedSession.setName(sessionNameEditText.getText().toString());
            Log.d(getString(R.string.TAG), "Saving new name of session " + updatedSession.getName());
            Log.d(getString(R.string.TAG), "Saving new name of session " + updatedSession.getId());
            db.sessionDao().update(updatedSession);
            Log.d(getString(R.string.TAG), "New name in database: " + db.sessionDao().get(currentSessionId).getName());
*/            sessionNameEditText.setInputType(0);
        }
    }

    public void onSortFilterMainButtonClicked(View view) {
        View sortFilterLinearLayout = findViewById(R.id.sortFilterMainLinearLayout);
        if (sortFilterLinearLayout.getVisibility() == View.GONE) {
            sortFilterLinearLayout.setVisibility(View.VISIBLE);
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
                String sessionName = ((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString();
                session = db.sessionDao().get(sessionName.replaceFirst("Session: ", ""));
                sessionNameMainEditText.setText(sessionName, TextView.BufferType.NORMAL);
            }
            updateRecyclerView();
        }
    }

    public void onMockMessageMainButtonClicked(View view) {
        startActivity(new Intent(this, MockNearbyMessageActivity.class));
    }

    private void updateFilterSpinner(String selection) {
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

        int position = 0;
        for (int i = 0; i < filterList.size(); i++) {
            if (filterList.get(position).equals(selection)) {
                position = i;
            }
        }
        filterSpinner.setSelection(position);
    }

    private void updateDatabase(String userData) {
        Log.d(getString(R.string.TAG), "New user encountered");
        Log.d(getString(R.string.TAG), userData);
        String[] dataLines = userData.split("\n");
        List<List<String>> data = new ArrayList<>();
        for (String line : dataLines) {
            data.add(Arrays.asList(line.split(",")));
        }
        Log.d(getString(R.string.TAG), data.get(0).get(0) + ", " + data.get(1).get(0) + ", " + data.get(2).get(0));
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
            user.addSessionId(currentSessionId);
            db.userDao().insert(user);
        }
        else {
            user.addSessionId(currentSessionId);
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

        unPublish();
        String message = new String(this.message.getContent()) + newMessage;
        newMessage = "";
        Log.i(getString(R.string.TAG), "Publishing message: " + message);
        this.message = new Message(message.getBytes());
        this.newMessage = new Message(newMessage.getBytes());
        Nearby.getMessagesClient(this).publish(this.message);
    }
    public void unPublish() {
        Log.i(getString(R.string.TAG), "UnPublishing message: " + this.message.getContent());
        Nearby.getMessagesClient(this).unpublish(message);
    }

    private void updateRecyclerView() {
        Log.d(getString(R.string.TAG),"UPDATING RECYCLER VIEW");

        String filterSpinner = ((Spinner) findViewById(R.id.filterMainSpinner)).getSelectedItem().toString();
        String sortSpinner = ((Spinner) findViewById(R.id.sortMainSpinner)).getSelectedItem().toString();
        Log.d(getString(R.string.TAG), "Filter method: " + filterSpinner);
        Log.d(getString(R.string.TAG), "Sort method: " + sortSpinner);

        usersRecyclerView = findViewById(R.id.sessionUsersMainRecyclerView);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(db,
                sortUsers(filterUsers(filterSpinner), sortSpinner));
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

    private List<User> filterUsers(String filter) {
        switch (filter) {
            case "None":
                List<User> users = getUsersInSession(currentSessionId);
                //users.remove(new User(USER_ID, "", ""));
                return users;
            case "Favorite":
                return db.userDao().getFavorite(true);
            default:
                Session currentSession = db.sessionDao().get(filter.replaceFirst("Session: ", ""));
                Log.d(getString(R.string.TAG), "Showing users in session " + currentSession.getId() + ": " + currentSession.getName());
                this.currentSessionId = currentSession.getId();
                return getUsersInSession(currentSessionId);
        }
    }

    private List<User> getUsersInSession(int sessionId) {
        List<User> users = new ArrayList<>();
        for (User user : db.userDao().getAll()) {
            if (user.getSessionIds().contains(currentSessionId))
                users.add(user);
        }
        return users;
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
                        int age = Utilities.getCourseAge(Integer.toString(course.getYear()), quarterMap.get(course.getQuarter()).toLowerCase(), currentQuarterYear[1], currentQuarterYear[0]);
                        age = 5 - age;
                        if (age < 1) {
                            age = 1;
                        }
                        priority += age;
                    }
                    break;
                case "Course Size":
                    for (Course course : db.courseDao().getForUser(user.getId())) {
                        priority += course.getSize();
                    }
                    break;
                default:
                    priority = sharedClasses;
            }
            Log.d(getString(R.string.TAG), "User " + user.getName() + "with priority " + priority);
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
                i--;
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