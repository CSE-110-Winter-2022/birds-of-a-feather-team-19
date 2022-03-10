package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddSessionSimpleTest {
    private AppDatabase db;
    Session one, two, three;

    @Before
    public void createDbSessionAndAddToDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.useTestSingleton(context);

        one = new Session("testSessionOne");
        two = new Session("testSessionTwo");
        three = new Session("testSessionThree");

        db.sessionDao().insert(one);
        db.sessionDao().insert(two);
        db.sessionDao().insert(three);
    }

    @Test
    public void createSessionAndAddToDb() throws Exception {
        assertEquals(3, db.sessionDao().count());
    }

    @Test
    public void testUniqueId() throws Exception {
        assertNotEquals(db.sessionDao().get("testSessionOne").getId(),
                db.sessionDao().get("testSessionTwo").getId());
        assertNotEquals(db.sessionDao().get("testSessionOne").getId(),
                db.sessionDao().get("testSessionThree").getId());
        assertNotEquals(db.sessionDao().get("testSessionTwo").getId(),
                db.sessionDao().get("testSessionThree").getId());
    }

    @Test
    public void testSetSessionName() throws Exception {
        db.sessionDao().update(db.sessionDao().get("testSessionOne").getId(), "one");

        assertNotNull(db.sessionDao().get("one"));
        assertNull(db.sessionDao().get("testSessionOne"));
    }

    @After
    public void closeDB() throws IOException {
        db.close();
    }
}
