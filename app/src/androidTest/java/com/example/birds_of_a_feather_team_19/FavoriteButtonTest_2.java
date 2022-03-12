package com.example.birds_of_a_feather_team_19;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
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
public class FavoriteButtonTest_2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void favoriteButtonTest_2() {
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
        appCompatEditText3.perform(replaceText("110"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.enterAddCourseButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.doneAddCourseButton), withText("Done"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.mockMessageMainButton), withText("Mock"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.userDetailMockNearbyMessageEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("a4ca50b6-941b-11ec-b909-0242ac120002,,,,\nBill,,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n2021,FA,CSE,210,Small\n2022,WI,CSE,110,Large"), closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.enterMockNearbyMessageButton), withText("Enter"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.button), withText("BACK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.favoriteUserRowButton),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.sessionUsersMainRecyclerView)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.favoriteUserRowButton),
                        childAtPosition(
                                allOf(withId(R.id.personsRowsLayout),
                                        childAtPosition(
                                                withId(R.id.sessionUsersMainRecyclerView),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.unfavoriteUserRowButton),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.sessionUsersMainRecyclerView)))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.sessionUsersMainRecyclerView),
                        childAtPosition(
                                withId(R.id.container),
                                2)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.unfavoriteUserDetailButton),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.unfavoriteUserDetailButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.goBackUserDetailButton), withText("GO BACK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.favoriteUserRowButton),
                        withParent(allOf(withId(R.id.personsRowsLayout),
                                withParent(withId(R.id.sessionUsersMainRecyclerView)))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));
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
