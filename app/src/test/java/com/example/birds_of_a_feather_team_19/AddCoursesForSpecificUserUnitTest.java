package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddCoursesForSpecificUserUnitTest {
    private AppDatabase db;
    private User test;
    private Course one, two, three;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.singleton(context);

        test = new User("Test one", "");
        one = new Course(test.getId(), "2022", "WI", "CSE", "110");
        two = new Course(test.getId(), "2021", "FA", "CSE", "100");
        three = new Course(test.getId(), "2021", "FA", "CSE", "105");


        db.userDao().insert(test);
        db.courseDao().insert(one);
        db.courseDao().insert(two);
        db.courseDao().insert(three);

    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void addCoursesForSpecificUser() throws Exception {
        List<Course> storedClasses = db.courseDao().getForUser(test.getId());

        assertEquals(3, storedClasses.size());
        assertTrue(storedClasses.contains(one));
        assertTrue(storedClasses.contains(two));
        assertTrue(storedClasses.contains(three));
    }
}
