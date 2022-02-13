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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
            EditText subject = activity.findViewById(R.id.subjectAddCourseEditText);
            EditText number = activity.findViewById(R.id.numberAddCourseEditText);
            Button enterButton = activity.findViewById(R.id.enterAddCourseButton);

            subject.setText("CSE");
            number.setText("101");
            enterButton.performClick();
            number.setText("110");
            enterButton.performClick();

            Set<List<String>> expected = new HashSet<>();
            List<String> course = new ArrayList<>();
            course.add("2022");
            course.add("spring");
            course.add("cse");
            course.add("101");
            expected.add(course);
            course = new ArrayList<>();
            course.add("2022");
            course.add("spring");
            course.add("cse");
            course.add("110");
            expected.add(course);
            assertEquals(expected, activity.courses);
        });
    }
    @Test
    public void test_adds_duplicate_classes() {
        ActivityScenario<AddCourseActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText subject = activity.findViewById(R.id.subjectAddCourseEditText);
            EditText number = activity.findViewById(R.id.numberAddCourseEditText);
            Button enterButton = activity.findViewById(R.id.enterAddCourseButton);

            subject.setText("CSE");
            number.setText("101");
            enterButton.performClick();
            number.setText("101");
            enterButton.performClick();

            Set<List<String>> expected = new HashSet<>();
            List<String> course = new ArrayList<>();
            course.add("2022");
            course.add("spring");
            course.add("cse");
            course.add("101");
            expected.add(course);
            assertEquals(expected, activity.courses);
        });
    }
    @Test
    public void test_no_subject_input() {
        ActivityScenario<AddCourseActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText number = activity.findViewById(R.id.numberAddCourseEditText);
            Button enterButton = activity.findViewById(R.id.enterAddCourseButton);

            number.setText("101");
            enterButton.performClick();

            Set<List<String>> expected = new HashSet<>();
            assertEquals(expected, activity.courses);
        });
    }

    @Test
    public void test_no_number_input() {
        ActivityScenario<AddCourseActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText subject = activity.findViewById(R.id.subjectAddCourseEditText);
            Button enterButton = activity.findViewById(R.id.enterAddCourseButton);

            subject.setText("CSE");
            enterButton.performClick();

            Set<List<String>> expected = new HashSet<>();
            assertEquals(expected, activity.courses);
        });
    }

    @Test
    public void test_no_class_input() {
        ActivityScenario<AddCourseActivity> scenario = scenarioRule.getScenario();
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            Button enterButton = activity.findViewById(R.id.enterAddCourseButton);

            enterButton.performClick();

            Set<List<String>> expected = new HashSet<>();
            assertEquals(expected, activity.courses);
        });
    }

    

}
