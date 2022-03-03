package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
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
public class AddUserUnitTest {
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.useTestSingleton(context);
    }

    @Test
    public void createUserAndAddToDb() throws Exception {
        User one = new User("Test User One", "");
        User two = new User("Test User One", "");

        db.userDao().insert(one);
        db.userDao().insert(two);

        assertEquals(2, db.userDao().getAll().size());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

}
