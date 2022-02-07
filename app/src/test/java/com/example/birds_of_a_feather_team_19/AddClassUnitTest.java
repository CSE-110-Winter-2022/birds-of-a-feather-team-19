package com.example.birds_of_a_feather_team_19;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ibm.icu.impl.duration.impl.Utils;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddClassUnitTest {
    @Rule
    public ActivityScenarioRule<AddClassActivity> scenarioRule = new ActivityScenarioRule<>(AddClassActivity.class);


    @Test
    public void test_adds_two_cse_classes() {
        // This is an INTEGRATION test, as we're testing multiple units!
        // This test SHOULD fail. You need to fix it as an exercise!

        ActivityScenario<AddClassActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {

            EditText subject = (EditText) activity.findViewById(R.id.editTextSubject);
            EditText courseNumber = (EditText) activity.findViewById(R.id.editTextCourseNumber);

            Button enterButton = activity.findViewById(R.id.buttonEnter);
            Button doneButton = activity.findViewById(R.id.buttonDone);


            subject.setText("CSE");
            courseNumber.setText("42");

            enterButton.performClick();

            courseNumber.setText("110");

            enterButton.performClick();

            doneButton.performClick();

            TextView resultView = activity.findViewById(R.id.course_list_view);

            String result = (resultView.getText().toString());
            assertEquals("2022springcse110\n2022springcse42\n", result);
        });
    }
}