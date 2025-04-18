package com.example.myapplication;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.assertion.ViewAssertions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class SystemTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigateToDashboard() {
        // click on the Dashboard navigation item
        onView(withId(R.id.navigation_dashboard))
                .perform(click());

        // check if the text is correct (same as in DashboardViewModel)
        ViewInteraction dashboard = onView(withText("This is dashboard fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigateToHome() {
        // click on the Home navigation item
        onView(withId(R.id.navigation_home))
                .perform(click());

        // check if the text is correct (same as in HomeViewModel)
        onView(withText("This is home fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigateToNotifications() {
        // Click on the Notifications navigation item
        onView(withId(R.id.navigation_notifications))
                .perform(click());

        // check if the text is correct (same as in NotificationsViewModel)
        onView(withText("This is notifications fragment")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}