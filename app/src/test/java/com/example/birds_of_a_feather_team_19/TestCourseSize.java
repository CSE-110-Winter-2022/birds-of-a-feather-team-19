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
public class TestCourseSize {
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.useTestSingleton(context);
    }


    @Test
    public void createCourseAndCheckSize() throws Exception {
        String userId = "1";
        Course one = new Course(userId, 2022, 1, "CSE",
                "100", 0.33);
        Course two = new Course(userId, 2021, 9, "POLI",
                "27", 0.10);
        Course three = new Course(userId, 2022, 1, "MUS",
                "114", 0.06);

        one.setId(1);
        two.setId(2);
        three.setId(3);

        db.courseDao().insert(one);
        db.courseDao().insert(two);
        db.courseDao().insert(three);

        assertEquals(0.33, db.courseDao().get(1).getSize(), 0);
        assertEquals(0.10, db.courseDao().get(2).getSize(), 0);
        assertEquals(0.06, db.courseDao().get(3).getSize(), 0);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

}
