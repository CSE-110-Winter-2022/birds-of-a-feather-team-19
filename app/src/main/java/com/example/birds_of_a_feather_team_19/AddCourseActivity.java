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

import java.util.Set;
import java.util.TreeSet;

public class AddCourseActivity extends AppCompatActivity {
    public Set<String[]> courses = new TreeSet<>();
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
        String year = ((Spinner) findViewById(R.id.yearAddCourseSpinner)).getSelectedItem().toString();
        String quarter = ((Spinner) findViewById(R.id.quarterAddCourseSpinner)).getSelectedItem().toString();
        String subject = ((TextView) findViewById(R.id.subjectAddCourseEditText)).getText().toString();
        String number = ((TextView) findViewById(R.id.numberAddCourseEditText)).getText().toString();
        if (subject.isEmpty()) {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (number.isEmpty()) {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        String[] course = {year, quarter, subject, number};
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
        db.userDao().insert(new User(1, preferences.getString("name", null), preferences.getString("photo", null)));
        for (String[] course : courses) {
            db.courseDao().insert(new Course(0, course[0], course[1], course[2], course[3]));
        }

        finish();
    }
}