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

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class AddCourseActivity extends AppCompatActivity {
    public Set<String> courses = new TreeSet<>();
    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        Spinner yearSpinner = findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> yearAdapter =
                ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        Spinner quarterSpinner = findViewById(R.id.spinnerTerm);
        ArrayAdapter<CharSequence> quarterAdapter =
                ArrayAdapter.createFromResource(this, R.array.quarter, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);

        db = AppDatabase.singleton(this);
    }

    public void onEnterClicked(View view) {
        if (((TextView) findViewById(R.id.editTextSubject)).getText().toString().equals("")) {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (((TextView) findViewById(R.id.editTextNumber)).getText().toString().equals("")) {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        if (!courses.add((((Spinner) findViewById(R.id.spinnerYear)).getSelectedItem().toString() +
                ((Spinner) findViewById(R.id.spinnerTerm)).getSelectedItem().toString() +
                ((TextView) findViewById(R.id.editTextSubject)).getText().toString() +
                ((TextView) findViewById(R.id.editTextNumber)).getText().toString()).toLowerCase())) {
            return;
        }
        Toast.makeText(this, R.string.toast_class_added, Toast.LENGTH_SHORT).show();
    }

    public void onDoneClicked(View view) {
        if (courses.isEmpty()) {
            Utilities.showAlert(this, "Please enter a course");
            return;
        }

        SharedPreferences preferences = getSharedPreferences("Birds of a Feather", MODE_PRIVATE);
        db.userDao().insert(new User(1, preferences.getString("name", null), preferences.getString("photoURL", "")));

        for (String course : courses) {
            db.courseDao().insert(new Course(1, course));
        }

        finish();
    }


}