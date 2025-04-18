package com.cs309.testing;

import com.cs309.testing.View.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class SystemTest2 {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Start the server and run this test
     *
     * This test uses the default string value specified within the activity
     * instead of the input string from edittext
     *
     * the default string value is set by activityScenarioRule upon activity creation
     * meanwhile the switch is set to reading the default value
     */
    @Test
    public void reverseDefaultString(){
        String testString = "defaultstring";
        String resultString = "gnirtstluafed";

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.defaultString = testString;
            activity.aSwitch.setChecked(true);
        });

        onView(withId(R.id.submit)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));
    }

    /**
     * Start the server and run this test
     *
     * This test uses the user input string value from edittext
     * instead of the default string within the activity
     *
     * the default string value is set to null by activityScenarioRule upon activity creation
     * meanwhile the switch is set to reading the user input value
     */
    @Test
    public void reverseInputString(){

        String testString = "inputstring";
        String resultString = "gnirtstupni";

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.defaultString = null;
            activity.aSwitch.setChecked(false);
        });

        // Type in testString and send request
        onView(withId(R.id.stringEntry)).perform(typeText(testString), closeSoftKeyboard());

        // Click button to submit
        onView(withId(R.id.submit)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));
    }

    /**
     * Start the server and run this test
     *
     * This test uses the default string value specified within the activity
     * instead of the input string from edittext
     *
     * the default string value is set by activityScenarioRule upon activity creation
     * meanwhile the switch is set to reading the default value
     */
    @Test
    public void capitalizeDefaultString() {

        String testString = "defaultstring";
        String resultString = "DEFAULTSTRING";

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.defaultString = testString;
            activity.aSwitch.setChecked(true);
        });

        onView(withId(R.id.submit2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));
    }

    /**
     * Start the server and run this test
     *
     * This test uses the user input string value from edittext
     * instead of the default string within the activity
     *
     * the default string value is set to null by activityScenarioRule upon activity creation
     * meanwhile the switch is set to reading the user input value
     */
    @Test
    public void capitalizeInputString() {

        String testString = "inputstring";
        String resultString = "INPUTSTRING";

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.defaultString = null;
            activity.aSwitch.setChecked(false);
        });

        // Type in testString and send request
        onView(withId(R.id.stringEntry)).perform(typeText(testString), closeSoftKeyboard());

        // Click button to submit
        onView(withId(R.id.submit2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        // Verify that volley returned the correct value
        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));
    }

}

