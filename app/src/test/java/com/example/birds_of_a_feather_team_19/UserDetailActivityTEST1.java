package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class UserDetailActivityTEST1 {
    private AppDatabase db;
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = AppDatabase.singleton(context);
        User one = new User("Dude", "");
        User two = new User("Guy", "");
        User three = new User("Student", "");

        db.userDao().insert(one);
        db.userDao().insert(two);
        db.userDao().insert(three);

        db.courseDao().insert(new Course(one.getId(), "2021", "Fall", "CSE", "110"));
        db.courseDao().insert(new Course(one.getId(), "2021", "Fall", "CSE", "130"));

        db.courseDao().insert(new Course(two.getId(), "2021", "Fall", "CSE", "110"));

        db.courseDao().insert(new Course(three.getId(), "2021", "Fall", "CSE", "130"));
        db.courseDao().insert(new Course(three.getId(), "2021", "Fall", "MMW", "15"));

    }
    @After
    public void closeDb() throws IOException {
        db.close();
    }
    @Test
    public void createUserAndAddToDb() throws Exception {
//        Context context = view.getContext();
//        Intent intent = new Intent(context, UserDetailActivity.class);
//        intent.putExtra("id", 1);
//        context.startActivity(intent);

    }
}

