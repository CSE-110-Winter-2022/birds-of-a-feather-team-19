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
    public Set<String> courses = new TreeSet<>();
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Spinner yearSpinner = findViewById(R.id.spinnerYearAddCourse);
        ArrayAdapter<CharSequence> yearAdapter =
                ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        Spinner quarterSpinner = findViewById(R.id.spinnerTermAddCourse);
        ArrayAdapter<CharSequence> quarterAdapter =
                ArrayAdapter.createFromResource(this, R.array.quarter, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        db = AppDatabase.singleton(this);
    }

    public void onEnterAddCourseClicked(View view) {
        if (((TextView) findViewById(R.id.editTextSubjectAddCourse)).getText().toString().equals("")) {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (((TextView) findViewById(R.id.editTextNumberAddCourse)).getText().toString().equals("")) {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        if (!courses.add((((Spinner) findViewById(R.id.spinnerYearAddCourse)).getSelectedItem().toString() +
                ((Spinner) findViewById(R.id.spinnerTermAddCourse)).getSelectedItem().toString() +
                ((TextView) findViewById(R.id.editTextSubjectAddCourse)).getText().toString() +
                ((TextView) findViewById(R.id.editTextNumberAddCourse)).getText().toString()).toLowerCase())) {
            return;
        }
        Toast.makeText(this, R.string.toast_class_added, Toast.LENGTH_SHORT).show();
    }

    public void onDoneAddCourseClicked(View view) {
        if (courses.isEmpty()) {
            Utilities.showAlert(this, "Please enter a course");
            return;
        }

        SharedPreferences preferences = getSharedPreferences("Birds of a Feather", MODE_PRIVATE);
        db.userDao().insert(new User(0, preferences.getString("name", null), preferences.getString("photoURL", "")));

        for (String course : courses) {
            db.courseDao().insert(new Course(0, course, course, course, course));
        }

        finish();
    }


}