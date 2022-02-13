package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.*;
import android.os.Build;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;


@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class CourseEqualityUnitTest {
    @Test
    public void twoCoursesAreEqual() throws Exception {
        int userId = 1;
        Course one = new Course(userId, "2022", "WI", "CSE", "110");
        Course two = new Course(userId, "2022", "WI", "CSE", "110");
        Course three = new Course(userId, "2021", "FA", "ECON", "109");

        assertTrue(one.equals(two));
    }

    @Test
    public void twoCoursesAreNotEqual() throws Exception {
        int userId = 1;
        Course one = new Course(userId, "2022", "WI", "CSE", "110");
        Course two = new Course(userId, "2021", "FA", "ECON", "109");

        assertFalse(one.equals(two));
    }
}
