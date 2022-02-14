package com.example.birds_of_a_feather_team_19;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddMultipleUsersTest {
    private String[] testMockUsers;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void clearDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase db = AppDatabase.singleton(context);
        db.userDao().deleteAll();
        db.courseDao().deleteAll();

        User testUser = new User(1, "Team 19", "");
        db.userDao().insert(testUser);

        Course testCourse1 = new Course(1, "2022", "winter", "cse", "110");
        Course testCourse2 = new Course(1, "2021", "fall", "cse", "105");
        Course testCourse3 = new Course(1, "2021", "spring", "cse", "30");

        db.courseDao().insert(testCourse1);
        db.courseDao().insert(testCourse2);
        db.courseDao().insert(testCourse3);
    }

    @Before
    public void setUpTestUsers() {
        testMockUsers = new String[]{"Bill,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n2022,WI,CSE,110",
                "Will,,,\n" +
                        ",,,\n" +
                        "2022,WI,CSE,110\n" +
                        "2020,FA,CSE,12\n" +
                        "2022,SP,CSE,110",
                "Dill,,,\n" +
                        "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n" +
                        "2022,WI,CSE,110\n" +
                        "2020,FA,CSE,12\n" +
                        "2022,SP,CSE,110\n" +
                        "2021,SP,CSE,30\n" +
                        "2021,FA,CSE,105",
                "Jill,,,\n" +
                        ",,,\n" +
                        "2020,FA,CSE,120\n" +
                        "2022,SP,CSE,110"
        };
    }

    @Test
    public void addMultipleUsersTest() {
//        ViewInteraction materialButton7 = onView(
//                allOf(withId(R.id.mockMessageMainButton), withText("Mock"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        materialButton7.perform(click());
//
//        ViewInteraction appCompatEditText10 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText10.perform(replaceText("Bill,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n2021,FA,CSE,210\n2022,WI,CSE,110\n2022,SP,CSE,110"), closeSoftKeyboard());
//
//        ViewInteraction materialButton8 = onView(
//                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton8.perform(click());
//
//        ViewInteraction appCompatEditText11 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText11.perform(click());
//
//        ViewInteraction appCompatEditText12 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText12.perform(replaceText("Will,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n2022,WI,CSE,110\n2020,FA,CSE,12\n2022,SP,CSE,110"), closeSoftKeyboard());
//
//        ViewInteraction materialButton9 = onView(
//                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton9.perform(click());
//
//        ViewInteraction appCompatEditText13 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText13.perform(click());
//
//        ViewInteraction appCompatEditText14 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText14.perform(replaceText("Dill,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n2022,WI,CSE,110\n2020,FA,CSE,12\n2022,SP,CSE,110\n2021,SP,CSE,30\n2021,FA,CSE,105"), closeSoftKeyboard());
//
//        ViewInteraction materialButton10 = onView(
//                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton10.perform(click());
//
//        ViewInteraction appCompatEditText15 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText15.perform(click());
//
//        ViewInteraction appCompatEditText16 = onView(
//                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatEditText16.perform(replaceText("Jill,,,\n,,,\n2020,FA,CSE,120\n2022,SP,CSE,110"), closeSoftKeyboard());
//
//        ViewInteraction materialButton11 = onView(
//                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton11.perform(click());
//
//        ViewInteraction materialButton12 = onView(
//                allOf(withId(R.id.button), withText("BACK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                2),
//                        isDisplayed()));
//        materialButton12.perform(click());
//
//
//        ViewInteraction materialButton13 = onView(
//                allOf(withId(R.id.startStopMainButton), withText("Start"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        materialButton13.perform(click());
//
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.nameUserRowTextView), withText("Dill (3)"),
//                        withParent(allOf(withId(R.id.personsRowsLayout),
//                                withParent(withId(R.id.usersMainRecyclerView)))),
//                        isDisplayed()));
//        textView.check(matches(withText("Dill (3)")));
//
//        ViewInteraction recyclerView = onView(
//                allOf(withId(R.id.usersMainRecyclerView),
//                        childAtPosition(
//                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
//                                0)));
//        recyclerView.perform(actionOnItemAtPosition(0, click()));
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.titleCourseRowTextView), withText("2021 spring cse 30"),
//                        withParent(allOf(withId(R.id.constraintLayout),
//                                withParent(withId(R.id.coursesUserDetailRecyclerView)))),
//                        isDisplayed()));
//        textView2.check(matches(withText("2021 spring cse 30")));
//
//        ViewInteraction textView3 = onView(
//                allOf(withId(R.id.titleCourseRowTextView), withText("2022 winter cse 110"),
//                        withParent(allOf(withId(R.id.constraintLayout),
//                                withParent(withId(R.id.coursesUserDetailRecyclerView)))),
//                        isDisplayed()));
//        textView3.check(matches(withText("2022 winter cse 110")));
//
//        ViewInteraction materialButton114 = onView(
//                allOf(withId(R.id.goBackUserDetailButton), withText("GO BACK"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                0),
//                        isDisplayed()));
//        materialButton114.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
