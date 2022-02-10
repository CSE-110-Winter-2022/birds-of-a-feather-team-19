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

public class AddClassActivity extends AppCompatActivity {
    private Set<String> courses = new TreeSet<>();
    private AppDatabase db;
    private User user;

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
    }

    public void onEnterClicked(View view) {
        if (((TextView) findViewById(R.id.editTextSubject)).getText().toString() == "") {
            Utilities.showAlert(this, "Please enter course subject");
            return;
        }
        if (((TextView) findViewById(R.id.editTextNumber)).getText().toString() == "") {
            Utilities.showAlert(this, "Please enter course number");
            return;
        }

        String courseName = ((Spinner) findViewById(R.id.spinnerYear)).getSelectedItem().toString() +
                ((Spinner) findViewById(R.id.spinnerTerm)).getSelectedItem().toString() +
                ((TextView) findViewById(R.id.editTextSubject)).getText().toString() +
                ((TextView) findViewById(R.id.editTextNumber)).getText().toString();
        courseName = courseName.toLowerCase();

        courses.add(courseName);
        Toast.makeText(this, "Class added", Toast.LENGTH_SHORT).show();
    }

    public void onDoneClicked(View view) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String name = preferences.getString("name", "Error!");
        String photo = preferences.getString("photo", "");
        db.usersDao().insert(new User(name, photo));

        for (String course : courses) {
            //int newCourseId = db.courseDao().maxId() + 1;
            //Course newCourse = new Course(newCourseId, 0, course);
            //db.courseDao().insert(newCourse);
            // System.out.println(userId + ", " + newCourseId + ", " + course);
            db.courseDao().insert(new Course(0, course));
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