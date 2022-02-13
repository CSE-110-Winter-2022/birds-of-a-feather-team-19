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
public class AddNameUnitTest {
    @Rule
    public ActivityScenarioRule<AddNameActivity> scenarioRule = new ActivityScenarioRule<>(AddNameActivity.class);

    @Test
    public void addNameTest() {
        ActivityScenario<AddNameActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText name = activity.findViewById(R.id.nameAddNameEditText);
            Button nameButton = activity.findViewById(R.id.confirmAddNameButton);
            name.setText("Jerry");
            nameButton.performClick();

            assertEquals("Jerry", name.getText().toString());

        });
    }


}
