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

import java.util.Set;
import java.util.TreeSet;

public class AddClassActivity extends AppCompatActivity {
    private Set<String> courses = new TreeSet<String>();
    private AppDatabase db;
    private IUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        int userId = getIntent().getIntExtra("user_id", 0);

        db = AppDatabase.singleton(this);
        user = db.userWithCoursesDao().get(userId);

//        setTitle(user.getName());

        Spinner yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
        ArrayAdapter<CharSequence> yearAdapter =
                ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


        Spinner quarterSpinner = (Spinner) findViewById(R.id.spinnerQuarter);
        ArrayAdapter<CharSequence> quarterAdapter =
                ArrayAdapter.createFromResource(this, R.array.quarter, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);


    }


    public void onEnterClicked(View view) {
        String courseName = "";
        Spinner yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
        Spinner quarterSpinner = (Spinner) findViewById(R.id.spinnerQuarter);
        TextView subject = (TextView) findViewById(R.id.editTextSubject);
        TextView courseNumber = (TextView) findViewById(R.id.editTextCourseNumber);
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
        /* for each element in courses, add to db;*/
//        int userId = user.getId();
        for (String course : courses) {
            System.out.println(course);
            int newCourseId = db.courseDao().maxId() + 1;
            Course newCourse = new Course(newCourseId, 0, course);
            db.courseDao().insert(newCourse);
            // System.out.println(userId + ", " + newCourseId + ", " + course);
        }
        // need to somehow implement user functionality
//        for (Course course : db.courseDao().getForUser(0)) {
//            System.out.println(course.course);
//        }
        // Intent intent .....
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putStringSet("courses_list", courses);

        editor.apply();

        String s = "";

        for (String str : courses) {
            s += str + "\n";
            System.out.println(str);
        }
        TextView courses_view = (TextView) findViewById(R.id.course_list_view);
        courses_view.setText(s);

//        finish();
    }
}