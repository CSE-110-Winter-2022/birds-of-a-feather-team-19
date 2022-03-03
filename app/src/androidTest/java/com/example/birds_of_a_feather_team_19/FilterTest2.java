package com.example.birds_of_a_feather_team_19;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FilterTest2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void filterTest2() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.nameAddNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Team19"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.confirmAddNameButton), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.submitAddPhotoURLButton), withText("SUBMIT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.quarterAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(5);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.subjectAddCourseEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("CSE"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.numberAddCourseEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("140"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.enterAddCourseButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.yearAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.quarterAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner3.perform(click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("140"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("110"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("110"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatSpinner4 = onView(
                allOf(withId(R.id.sizeAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatSpinner4.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.enterAddCourseButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatSpinner5 = onView(
                allOf(withId(R.id.quarterAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner5.perform(click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(4);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("110"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("105"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("105"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.enterAddCourseButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatSpinner6 = onView(
                allOf(withId(R.id.yearAddCourseSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatSpinner6.perform(click());

        DataInteraction appCompatCheckedTextView6 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("105"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("12"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.numberAddCourseEditText), withText("12"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.enterAddCourseButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.doneAddCourseButton), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.mockMessageMainButton), withText("Mock"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton8.perform(click());


        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("Will,,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n2021,SP,CSE,110,large\n2021,FA,CSE,105,large\n2020,FA,CSE,12,large"), closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton9.perform(click());



        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("Dill,,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n2022,WI,CSE,140,tiny\n2021,SP,CSE,130,tiny\n2020,FA,CSE,12,tiny"), closeSoftKeyboard());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton10.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText14.perform(click());



        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText16.perform(replaceText("Fill,,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n2020,FA,CSE,12,large"), closeSoftKeyboard());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton11.perform(click());

        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.button), withText("BACK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton12.perform(click());

        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.startStopMainButton), withText("Start"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton13.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Will (3)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView.check(matches(withText("Will (3)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Dill (2)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView2.check(matches(withText("Dill (2)")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Fill (1)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView3.check(matches(withText("Fill (1)")));

        ViewInteraction appCompatSpinner7 = onView(
                allOf(withId(R.id.sort_list_students_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner7.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        materialTextView.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Will (3)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView4.check(matches(withText("Will (3)")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Dill (2)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView5.check(matches(withText("Dill (2)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Fill (1)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView6.check(matches(withText("Fill (1)")));

        ViewInteraction appCompatSpinner8 = onView(
                allOf(withId(R.id.sort_list_students_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner8.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        materialTextView2.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Dill (2)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView7.check(matches(withText("Dill (2)")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Will (3)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView8.check(matches(withText("Will (3)")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Fill (1)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView9.check(matches(withText("Fill (1)")));

        ViewInteraction appCompatSpinner9 = onView(
                allOf(withId(R.id.sort_list_students_filter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatSpinner9.perform(click());

        DataInteraction materialTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        materialTextView3.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.nameUserRowTextView), withText("Dill (2)"),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.usersMainRecyclerView)))),
                        isDisplayed()));
        textView10.check(matches(withText("Dill (2)")));
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
