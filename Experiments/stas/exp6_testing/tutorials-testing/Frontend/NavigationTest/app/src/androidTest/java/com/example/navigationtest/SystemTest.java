package com.example.navigationtest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.view.Gravity;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class SystemTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testHomeFragment() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // click on the Dashboard navigation item
        onView(withId(R.id.nav_home)).perform(click());

        // check if the text is correct (same as in DashboardViewModel)
        ViewInteraction dashboard = onView(withText("This is home fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testGalleryFragment() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // click on the Dashboard navigation item
        onView(withId(R.id.nav_gallery)).perform(click());

        // check if the text is correct (same as in DashboardViewModel)
        ViewInteraction dashboard = onView(withText("This is gallery fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSlideshowFragment() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // click on the Dashboard navigation item
        onView(withId(R.id.nav_slideshow)).perform(click());

        // check if the text is correct (same as in DashboardViewModel)
        ViewInteraction dashboard = onView(withText("This is slideshow fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
