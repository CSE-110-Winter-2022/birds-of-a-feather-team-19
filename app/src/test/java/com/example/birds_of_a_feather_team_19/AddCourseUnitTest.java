package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddCourseUnitTest {
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.useTestSingleton(context);
    }

    @Test
    public void createCourseAndAddToDb() throws Exception {
        String userId = "1";
        Course one = new Course(userId, "2022", "WI", "CSE",
                "100", "small");
        Course two = new Course(userId, "2021", "FA", "POLI",
                "27", "small");
        Course three = new Course(userId, "2022", "WI", "MUS",
                "114", "small");

        db.courseDao().insert(one);
        db.courseDao().insert(two);
        db.courseDao().insert(three);

        assertEquals(3, db.courseDao().getForUser(userId).size());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }


}
