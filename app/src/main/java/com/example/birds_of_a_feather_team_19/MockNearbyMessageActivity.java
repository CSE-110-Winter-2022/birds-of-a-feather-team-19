package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.net.URL;

public class MockNearbyMessageActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_nearby_message);

        db = AppDatabase.singleton(this);
    }

    public void onButtonEnterMockNearbyMessageClicked(View view) {
        String userDetail = ((TextView) findViewById(R.id.editTextUserDetailMockNearbyMessage)).getText().toString();
        String userName = userDetail.substring(0, userDetail.indexOf("\n"));
        userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);
        String userPhotoURL = userDetail.substring(0, userDetail.indexOf("\n"));
        if (photoURLInvalid(((TextView) findViewById(R.id.photoAddPhotoURLEditText)).getText().toString())) {
            Utilities.showAlert(this, "Please enter a valid photo");
            return;
        }
        userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);
        User user = new User(0, userName, userPhotoURL);
        db.userDao().insert(user);

        while (!userDetail.isEmpty()) {
            String courseYear = userDetail.substring(0, userDetail.indexOf(","));
            userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
            String courseTerm = userDetail.substring(0, userDetail.indexOf(","));
            userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
            String courseSubject = userDetail.substring(0, userDetail.indexOf(","));
            userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
            String courseNumber = userDetail.substring(0, userDetail.indexOf("\n"));
            userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);

            switch (courseTerm) {
                case "FA":
                    courseTerm = "fall";
                    break;
                case "WI":
                    courseTerm = "winter";
                    break;
                case "SP":
                    courseTerm = "spring";
                    break;
                case "SS1":
                    courseTerm = "summer session 1";
                    break;
                case "SS2":
                    courseTerm = "summer session 2";
                    break;
                case "SSS":
                    courseTerm = "special summer session";
                    break;
                default:
                    Utilities.showAlert(this, "Please enter a valid course");
                    return;
            }
            db.courseDao().insert(new Course(user.getId(), courseYear, courseTerm, courseSubject, courseNumber));
        }

        finish();
    }

    private boolean photoURLInvalid(String photoURL) {
        if (!photoURL.equals("")) {
            try {
                new URL(photoURL).toURI();
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}