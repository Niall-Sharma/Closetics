package com.example.closetics;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * This testing file uses ActivityScenarioRule instead of ActivityTestRule
 * to demonstrate system testings cases
 */
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class StasSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     *
     */
    @Test
    public void logOutAndSignUpTest(){
        Intents.init();

        String initialTestUsername = "user1001";
        long initialTestId = 1;

        String testUsername = "mrTesty12487";
        String testEmail = "testymail@coolmail.org";
        String testPassword = "Pp12345@";

        // set a fake user
        activityScenarioRule.getScenario().onActivity(activity -> {
            UserManager.saveUsername(activity.getApplicationContext(), initialTestUsername);
            UserManager.saveUserID(activity.getApplicationContext(), initialTestId);
        });

        // open profile fragment
        onView(withId(R.id.navigation_profile)).perform(click());

        onView(withId(R.id.profile_username_text)).check(matches(withText(initialTestUsername)));

        // log out
        onView(withId(R.id.profile_logout_button)).perform(click());
        sleep();

        onView(withId(R.id.profile_username_text)).check(matches(withText("Guest (not logged in)")));

        // click sign up
        onView(withId(R.id.profile_signup_button)).perform(click());

        intended(hasComponent(SignupActivity.class.getName()));

        // type new user's data and sign up
        onView(withId(R.id.signup_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edit)).perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edit)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edit)).perform(typeText(testPassword), closeSoftKeyboard());

        onView(withId(R.id.signup_sq1_spinner))
                .perform(click());
        //.perform(swipeDown());
        //onView(withText("What is the name of your childhood best friend?")).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("What is the name of your childhood best friend?"))).perform(click());
        onView(withId(R.id.signup_sq1_edit)).perform(typeText("Roberto"), closeSoftKeyboard());

        onView(withId(R.id.signup_sq2_spinner)).perform(click());
        //onView(withText("What is your favorite color?")).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("What is your favorite color?"))).perform(click());
        onView(withId(R.id.signup_sq2_edit)).perform(typeText("orange"), closeSoftKeyboard());

        onView(withId(R.id.signup_signup_button)).perform(click());
        sleep();

        intended(hasComponent(MainActivity.class.getName()));

        // open profile fragment
        onView(withId(R.id.navigation_profile)).perform(click());

        onView(withId(R.id.profile_username_text)).check(matches(withText(testUsername)));

        // open delete user activity
        onView(withId(R.id.profile_delete_user_button)).perform(click());

        intended(hasComponent(DeleteUserActivity.class.getName()));

        // click 'yes'
        onView(withId(R.id.delete_user_yes_button)).perform(click());
        sleep();

        intended(hasComponent(MainActivity.class.getName()), times(2));

        // open profile fragment
        onView(withId(R.id.navigation_profile)).perform(click());

        // check that user is logged out
        onView(withId(R.id.profile_username_text)).check(matches(withText("Guest (not logged in)")));

        Intents.release();
    }



    private void sleep() {
        sleep(SIMULATED_DELAY_MS);
    }

    private void sleep(final int delay_ms) {
        try {
            Thread.sleep(delay_ms);
        } catch (InterruptedException e) {}
    }

}

