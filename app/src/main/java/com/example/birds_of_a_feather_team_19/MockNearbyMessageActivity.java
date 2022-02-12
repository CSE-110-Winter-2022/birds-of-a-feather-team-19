package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Index;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MockNearbyMessageActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_nearby_message);

        db = AppDatabase.singleton(this);
    }

    public void onEnterMockNearbyMessageButtonClicked(View view) {
        String userDetail = ((TextView) findViewById(R.id.userDetailMockNearbyMessageEditText)).getText().toString();
        String userName = userDetail.substring(0, userDetail.indexOf("\n"));
        if (db.userDao().get(userName) != null) {
            Utilities.showAlert(this, "Username taken. Please enter another username");
            return;
        }
        userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);
        String userPhotoURL = userDetail.substring(0, userDetail.indexOf("\n"));
        if (photoURLInvalid(userPhotoURL)) {
            Utilities.showAlert(this, "Please enter a valid photo");
            return;
        }
        userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);
        db.userDao().insert(new User(userName, userPhotoURL));

        userDetail = userDetail.toLowerCase() + "\n";
        String courseYear, courseQuarter, courseSubject, courseNumber;
        while (!userDetail.isEmpty()) {
            try {
                courseYear = userDetail.substring(0, userDetail.indexOf(","));
                userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
                courseQuarter = userDetail.substring(0, userDetail.indexOf(","));
                userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
                courseSubject = userDetail.substring(0, userDetail.indexOf(","));
                userDetail = userDetail.substring(userDetail.indexOf(",") + 1);
                courseNumber = userDetail.substring(0, userDetail.indexOf("\n"));
                userDetail = userDetail.substring(userDetail.indexOf("\n") + 1);
            } catch (IndexOutOfBoundsException e) {
                Utilities.showAlert(this, "Please enter valid courses");
                return;
            }

            switch (courseQuarter) {
                case "fa":
                    courseQuarter = "fall";
                    break;
                case "wi":
                    courseQuarter = "winter";
                    break;
                case "sp":
                    courseQuarter = "spring";
                    break;
                case "ss1":
                    courseQuarter = "summer session 1";
                    break;
                case "ss2":
                    courseQuarter = "summer session 2";
                    break;
                case "sss":
                    courseQuarter = "special summer session";
                    break;
                default:
                    Utilities.showAlert(this, "Please enter valid courses");
                    return;
            }
            db.courseDao().insert(new Course(db.userDao().get(userName).getId(), courseYear, courseQuarter, courseSubject, courseNumber));
        }

        finish();
    }

    private boolean photoURLInvalid(String photoURL) {
        ExecutorService imageExecutor = Executors.newSingleThreadExecutor();

        if (!photoURL.equals("")) {
            Future<Boolean> future = (imageExecutor.submit(() -> BitmapFactory.decodeStream(new URL(photoURL).openStream()) == null));
            try {
                return future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}