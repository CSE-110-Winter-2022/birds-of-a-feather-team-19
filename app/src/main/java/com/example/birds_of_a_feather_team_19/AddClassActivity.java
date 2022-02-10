package com.example.birds_of_a_feather_team_19;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AddClassActivity extends AppCompatActivity {
    private Set<String> courses = new TreeSet<String>();
    private AppDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        int userId = getIntent().getIntExtra("user_id", 0);

        db = AppDatabase.singleton(this);
        user = db.usersDao().get(userId);

//        setTitle(user.getName());

        Spinner yearSpinner = findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> yearAdapter =
                ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


        Spinner quarterSpinner = findViewById(R.id.spinnerQuarter);
        ArrayAdapter<CharSequence> quarterAdapter =
                ArrayAdapter.createFromResource(this, R.array.quarter, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);
    }

    public void onEnterClicked(View view) {
        String courseName = "";
        Spinner yearSpinner = findViewById(R.id.spinnerYear);
        Spinner quarterSpinner = findViewById(R.id.spinnerQuarter);
        TextView subject = findViewById(R.id.editTextSubject);
        TextView courseNumber = findViewById(R.id.editTextCourseNumber);
        if (subject.getText().toString() == null || courseNumber.getText().toString() == null) {
            return;
        }
        courseName = yearSpinner.getSelectedItem().toString()
                + quarterSpinner.getSelectedItem().toString()
                + subject.getText().toString()
                + courseNumber.getText().toString();
        courseName = courseName.toLowerCase();
        courses.add(courseName);

        Toast.makeText(this, "Class added", Toast.LENGTH_SHORT).show();
    }

    public void onDoneClicked(View view) {
        for (String course : courses) {
            Course newCourse = new Course(1, course);
            db.courseDao().insert(newCourse);
        }
//        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putStringSet("courses_list", courses);
//        editor.apply();
//        String s = "";
//        for (String str : courses) {
//            s += str + "\n";
//            System.out.println(str);
//        }
//        TextView courses_view = (TextView) findViewById(R.id.course_list_view);
//        courses_view.setText(s);
        finish();
    }


}