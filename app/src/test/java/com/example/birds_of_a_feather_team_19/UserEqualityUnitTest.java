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
public class UserEqualityUnitTest {
    @Test
    public void usersAreEqual() throws Exception {
        User one = new User("Test one", "");
        User two = one;

        assertTrue(one.equals(two));
    }

    @Test
    public void usersAreNotEqual() throws Exception {
        User one = new User("Test One", "");
        User two = new User("Test One", "");

        one.setId("1");
        two.setId("2");

        assertFalse(one.equals(two));
    }
}
