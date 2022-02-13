package com.example.birds_of_a_feather_team_19;

import static org.junit.Assert.assertEquals;

import android.app.AlertDialog;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddPhotoURLActivityTest extends TestCase {
    @Rule
    public ActivityScenarioRule<AddPhotoURLActivity> scenarioRule = new ActivityScenarioRule<>(AddPhotoURLActivity.class);

    @Test
    public void addNameTest() {
        ActivityScenario<AddPhotoURLActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText photoURL = activity.findViewById(R.id.photoAddPhotoURLEditText);
            Button URLButton = activity.findViewById(R.id.submitAddPhotoURLButton);
            photoURL.setText("Test URL Input");
            URLButton.performClick();

            assertEquals("Test URL Input", photoURL.getText().toString());
        });
    }

    @Test
    public void blankURLTest() {
        ActivityScenario<AddPhotoURLActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText photoURL = activity.findViewById(R.id.photoAddPhotoURLEditText);
            Button URLButton = activity.findViewById(R.id.submitAddPhotoURLButton);
            URLButton.performClick();

            assertEquals("", photoURL.getText().toString());
        });
    }

    @Test
    public void validPhotoUrlTest() {
        ActivityScenario<AddPhotoURLActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {

        });
    }
}