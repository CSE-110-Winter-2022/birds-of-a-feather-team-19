package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.*;
import android.os.Build;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.birds_of_a_feather_team_19.model.db.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class UserPriorityUnitTest {
    @Test
    public void testUserPriorityCompareTo() throws Exception {
        User one = new User(1, "Test user one", "");
        User two = new User(2, "Test user two", "");

        UserPriority upOne = new UserPriority(one, 1, 1);
        UserPriority upTwo = new UserPriority(two, 2, 2);

        assertEquals(1, upOne.compareTo(upTwo));
    }

    @Test
    public void userPriorityEqualsReturnsTrue() throws Exception {
        User one = new User(1, "Test user one", "");

        UserPriority upOne = new UserPriority(one, 1, 1);
        UserPriority upTwo = new UserPriority(one, 10, 10);

        assertTrue(upOne.equals(upTwo));
    }

    @Test
    public void userPriorityEqualsReturnsFalse() throws Exception {
        User one = new User(1, "Test user one", "");
        User two = new User(2, "Test user two", "");

        UserPriority upOne = new UserPriority(one, 1, 1);
        UserPriority upTwo = new UserPriority(two, 10, 10);

        assertFalse(upOne.equals(upTwo));
    }
}
