package com.example.birds_of_a_feather_team_19;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AddCourseActivity extends AppCompatActivity {
    public Set<List<String>> courses = new HashSet<>();
    private Map<String, Integer> quarterMap;
    private Map<String, Double> sizeMap;
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        quarterMap = new HashMap<>();
        quarterMap.put("Winter", 1);
        quarterMap.put("Spring", 3);
        quarterMap.put("Summer Session 1", 6);
        quarterMap.put("Summer Session 2", 8);
        quarterMap.put("Special Summer Session", 6);
        quarterMap.put("Fall", 9);
        sizeMap = new HashMap<>();
        sizeMap.put("Tiny (<40)", 1.00);
        sizeMap.put("Small (40-75)", 0.33);
        sizeMap.put("Medium (75-150)", 0.18);
        sizeMap.put("Large (150-250)", 0.10);
        sizeMap.put("Huge (250-400)", 0.06);
        sizeMap.put("Gigantic (400+)", 0.03);
        Log.d(getString(R.string.TAG), "Add course activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Spinner yearSpinner = findViewById(R.id.yearAddCourseSpinner);
        ArrayAdapter<CharSequence> yearAdapter =
                ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        Spinner quarterSpinner = findViewById(R.id.quarterAddCourseSpinner);
        ArrayAdapter<CharSequence> quarterAdapter =
                ArrayAdapter.createFromResource(this, R.array.quarter, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);
        Spinner sizeSpinner = findViewById(R.id.sizeAddCourseSpinner);
        ArrayAdapter<CharSequence> sizeAdapter =
                ArrayAdapter.createFromResource(this, R.array.size, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        db = AppDatabase.singleton(this);
    }

    public void onEnterAddCourseButtonClicked(View view) {
        String year = ((Spinner) findViewById(R.id.yearAddCourseSpinner)).getSelectedItem().toString();
        String quarter = ((Spinner) findViewById(R.id.quarterAddCourseSpinner)).getSelectedItem().toString();
        String subject = ((TextView) findViewById(R.id.subjectAddCourseEditText)).getText().toString().toUpperCase(Locale.ENGLISH);
        String number = ((TextView) findViewById(R.id.numberAddCourseEditText)).getText().toString().toUpperCase(Locale.ENGLISH);
        String size = ((Spinner) findViewById(R.id.sizeAddCourseSpinner)).getSelectedItem().toString();
        if (subject.isEmpty()) {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (number.isEmpty()) {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        List<String> course = new ArrayList<>();
        course.add(year);course.add(quarter);course.add(subject);course.add(number);course.add(size);
        if (!courses.add(course)) {
            Utilities.showAlert(this, "Course previously entered");
            return;
        }
        Log.d(getString(R.string.TAG), "Course added: " + year + quarter + subject + number);
        Toast.makeText(this, R.string.toast_add_course, Toast.LENGTH_SHORT).show();
    }

    public void onDoneAddCourseButtonClicked(View view) {
        if (courses.isEmpty()) {
            Utilities.showAlert(this, "Please enter a course");
            return;
        }

        SharedPreferences preferences = getSharedPreferences(getString(R.string.TAG), MODE_PRIVATE);
        db.userDao().insert(new User(MainActivity.USER_ID, preferences.getString("name", null), preferences.getString("photoURL", null)));
        for (List<String> course : courses) {
            db.courseDao().insert(new Course(MainActivity.USER_ID, Integer.parseInt(course.get(0)), quarterMap.get(course.get(1)), course.get(2), course.get(3), sizeMap.get(course.get(4))));
        }

        Log.d(getString(R.string.TAG), "Add course activity finished");

        finish();
    }
}