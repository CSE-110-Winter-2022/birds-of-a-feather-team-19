package com.example.birds_of_a_feather_team_19;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddCourseActivity extends AppCompatActivity {
    public Set<List<String>> courses = new HashSet<>();
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        db = AppDatabase.singleton(this);
    }

    public void onEnterAddCourseButtonClicked(View view) {
        String year = ((Spinner) findViewById(R.id.yearAddCourseSpinner)).getSelectedItem().toString().toLowerCase();
        String quarter = ((Spinner) findViewById(R.id.quarterAddCourseSpinner)).getSelectedItem().toString().toLowerCase();
        String subject = ((TextView) findViewById(R.id.subjectAddCourseEditText)).getText().toString().toLowerCase();
        String number = ((TextView) findViewById(R.id.numberAddCourseEditText)).getText().toString().toLowerCase();
        if (subject.isEmpty()) {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (number.isEmpty()) {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        List<String> course = new ArrayList<>();
        course.add(year);
        course.add(quarter);
        course.add(subject);
        course.add(number);
        if (!courses.add(course)) {
            Utilities.showAlert(this, "Course previously entered");
            return;
        }
        Toast.makeText(this, R.string.toast_add_course, Toast.LENGTH_SHORT).show();
    }

    public void onDoneAddCourseButtonClicked(View view) {
        if (courses.isEmpty()) {
            Utilities.showAlert(this, "Please enter a course");
            return;
        }

        SharedPreferences preferences = getSharedPreferences("Birds of a Feather", MODE_PRIVATE);
        db.userDao().insert(new User(1, preferences.getString("name", null), preferences.getString("photoURL", null)));
        for (List<String> course : courses) {
            db.courseDao().insert(new Course(1, course.get(0), course.get(1), course.get(2), course.get(3)));
        }

        finish();
    }
}