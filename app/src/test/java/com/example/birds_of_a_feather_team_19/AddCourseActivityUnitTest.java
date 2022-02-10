package com.example.birds_of_a_feather_team_19;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import android.os.Build;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.Set;
import java.util.TreeSet;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddCourseActivityUnitTest {

    @Rule
    public ActivityScenarioRule<AddCourseActivity> scenarioRule = new ActivityScenarioRule<>(AddCourseActivity.class);

    @Test
    public void test_adds_two_cse_classes() {
        ActivityScenario<AddCourseActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText subject = activity.findViewById(R.id.editTextSubject);
            EditText number = activity.findViewById(R.id.editTextNumber);
            Button enterButton = activity.findViewById(R.id.buttonEnter);

            subject.setText("CSE");
            number.setText("101");
            enterButton.performClick();
            number.setText("110");
            enterButton.performClick();

            Set<String> expected = new TreeSet<>();
            expected.add("2022springcse101");
            expected.add("2022springcse110");
            assertEquals(expected, activity.courses);
        });
    }
}