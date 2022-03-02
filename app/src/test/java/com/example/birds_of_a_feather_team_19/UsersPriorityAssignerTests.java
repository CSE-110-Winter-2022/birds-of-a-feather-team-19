package com.example.birds_of_a_feather_team_19;

import static com.example.birds_of_a_feather_team_19.Utilities.invalidName;
import static com.example.birds_of_a_feather_team_19.Utilities.invalidPhotoURL;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

        import static com.example.birds_of_a_feather_team_19.Utilities.invalidName;
        import static com.example.birds_of_a_feather_team_19.Utilities.invalidPhotoURL;
        import static org.junit.Assert.assertFalse;
        import static org.junit.Assert.assertTrue;

        import android.os.Build;

        import androidx.test.ext.junit.runners.AndroidJUnit4;

        import com.example.birds_of_a_feather_team_19.model.db.Course;
import static org.junit.Assert.assertEquals;

        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class UsersPriorityAssignerTests {
    //Empty String should return true
    private static Course c0, c1, c2, c3, c4, c5, c6, c7;
    private static List<Course> testCoursesList;

    @Before
    public void createTestClasses() {
        c0 = new Course(1, "2022", "winter", "CSE", "110");
        c1 = new Course(1, "2022", "winter", "CSE", "101");
        c2 = new Course(1, "2022", "fall", "CSE", "100");
        c3 = new Course(1, "2022", "fall", "BILD", "3");
        c4 = new Course(1, "2021", "spring", "LIGN", "101");
        c5 = new Course(1, "2021", "winter", "CSE", "21");
        c6 = new Course(1, "2021", "winter", "WCWP", "10A");
        c7 = new Course(1, "2020", "fall", "CSE", "11");
        testCoursesList = new ArrayList<Course>();
        testCoursesList.add(c0);
        testCoursesList.add(c1);
        testCoursesList.add(c2);
        testCoursesList.add(c3);
        testCoursesList.add(c4);
        testCoursesList.add(c5);
        testCoursesList.add(c6);
        testCoursesList.add(c7);
    }

    @Test
    public void testSharedClassesPriorityAssigner() {
        UserPriorityAssigner assigner = new SharedClassesPriorityAssigner();
        for (Course c : testCoursesList) {
            assertEquals(1, (int) assigner.getPriority(c));
        }
    }

    @Test
    public void testRecentClassPriorityAssigner() {
        UserPriorityAssigner assigner = new SharedRecentClassPriorityAssigner();
    }

    @Test
    public void testSharedClassSizeAssigner() {
        UserPriorityAssigner assigner = new SharedClassSizePriorityAssigner();

    }

    @Test
    public void testSharedThisQuarterPriorityAssigner() {
        UserPriorityAssigner assigner = new SharedThisQuarterPriorityAssigner();
        assertEquals(1, (int) assigner.getPriority(c0));
        assertEquals(1, (int) assigner.getPriority(c1));
        for (int i = 2; i < testCoursesList.size(); i++) {
            assertEquals(0, (int) assigner.getPriority(testCoursesList.get(i)));
        }
    }
}

