package com.example.closetics;


import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.StatisticsActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;

import static java.util.function.Predicate.not;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

// Mock the RequestServerForService class
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time

public class AshtenSystemTest {

    private static String adbPath = "C:\\Users\\ashte\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb.exe";
    private static final int SIMULATED_DELAY_MS = 500;
    private CountingIdlingResource idlingResource;

    private static String testUsername = "AshtenSystem";
    private static String testEmail = "ashtenSystemTest@coolman.com";
    private static String testPassword = "Abcde1234@";
    private static String securityPassword1 = "Cesar";
    private static String securityPassword2 = "Red";
    private static long userID;

    @ClassRule
    @Rule
    public static ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);



    /**
     * Create a test user!
     */
    //@BeforeClass
    public static void launchActivity() {
        //disableAnimations();
        /*
        ActivityScenario.launch(new Intent(Intent.ACTION_MAIN)
                .setClassName("com.example.closetics", "com.example.closetics.SignupActivity"));

        onView(withId(R.id.signup_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edit)).perform(typeText(testEmail), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edit)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.signup_confirm_edit)).perform(typeText(testPassword), closeSoftKeyboard());

        onView(withId(R.id.signup_sq1_spinner))
                .perform(click());

        onData(allOf(is(instanceOf(String.class)), is("What is the name of your childhood best friend?"))).perform(click());
        onView(withId(R.id.signup_sq1_edit)).perform(typeText(securityPassword1), closeSoftKeyboard());

        onView(withId(R.id.signup_sq2_spinner)).perform(click());

        onData(allOf(is(instanceOf(String.class)), is("What is your favorite color?"))).perform(click());
        onView(withId(R.id.editTextText2)).perform(typeText(securityPassword2), closeSoftKeyboard());

        onView(withId(R.id.signup_signup_button)).perform(click());
        sleep();
        userID = UserManager.getUserID(getApplicationContext());

        activityScenarioRule.getScenario().close();

         */
    }


    /**
     * Test a successful login with the newly created user
     */
    @Test
    public void testLogin(){
        performLogin();
        sleep();
        Intents.release();
        activityScenarioRule.getScenario().close();
    }


    /**
     * Successful change password test
     */
    @Test
    public void testChangePassword() {
        String newPassword = "Ppppp12345!";
        Intents.init();
        ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.navigation_profile)).check(matches(isDisplayed()))
                .perform(click());
        sleep();

        onView(withId(R.id.profile_logout_button)).check(matches(isClickable())).perform(click());
        sleep();

        onView(withId(R.id.profile_login_button)).check(matches(isDisplayed()))
                .perform(click());



        intended(hasComponent(LoginActivity.class.getName()));

        onView(withId(R.id.login_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.login_forgot_password)).perform(click());
        sleep();

        onView(withId(R.id.security_3_question_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("What is the name of your childhood best friend?"))).perform(click());
        sleep();

        onView(withId(R.id.security_input_password)).perform(typeText(securityPassword1), closeSoftKeyboard());
        onView(withId(R.id.change_password_edit)).perform(typeText(newPassword), closeSoftKeyboard());
        onView(withId(R.id.change_password_confirm_edit)).perform(typeText(newPassword), closeSoftKeyboard());

        onView(withId(R.id.change_submit_button)).perform(click());
        sleep();

        intended(allOf(hasComponent(MainActivity.class.getName()), hasAction(nullValue(String.class))));
        sleep();

        onView(withId(R.id.navigation_profile)).perform(click());
        sleep();

        onView(withId(R.id.profile_logout_button)).check(matches(isClickable()));
        sleep();

        onView(withId(R.id.profile_login_button)).perform(click());
        sleep();

        intended(hasComponent(LoginActivity.class.getName()), times(2));

        onView(withId(R.id.login_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.login_forgot_password)).perform(click());
        sleep();

        onView(withId(R.id.security_3_question_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("What is your favorite color?"))).perform(click());
        sleep();

        onView(withId(R.id.security_input_password)).perform(typeText(securityPassword2), closeSoftKeyboard());
        onView(withId(R.id.change_password_edit)).perform(typeText(testPassword), closeSoftKeyboard());
        onView(withId(R.id.change_password_confirm_edit)).perform(typeText(testPassword), closeSoftKeyboard());

        onView(withId(R.id.change_submit_button)).perform(click());
        sleep();

        List<Intent> allIntents = Intents.getIntents();

        long mainActivityLaunchCount = allIntents.stream()
                .filter(intent -> MainActivity.class.getName().equals(intent.getComponent().getClassName()))
                .count();

        assertEquals(3, mainActivityLaunchCount);

        Intents.release();
        activityScenarioRule.getScenario().close();
    }

    @Test
    public void editUser(){
        performLogin();
        sleep();

        String newUsername = "SystemAshten";

        onView(withId(R.id.navigation_profile)).perform(click());
        sleep();

        onView(withId(R.id.profile_edit_user_button)).perform(click());
        sleep();

        intended(hasComponent(EditUserActivity.class.getName()));

        onView(withId(R.id.change_username_edit)).perform(typeText(newUsername), closeSoftKeyboard());
        onView(withId(R.id.change_email_new_edit)).perform(typeText(testEmail), closeSoftKeyboard());

        onView(withId(R.id.edit_user_submit)).perform(click());
        sleep();

        onView(withId(R.id.navigation_profile)).perform(click());
        sleep();

        onView(withId(R.id.profile_username_text)).check(matches(withText(newUsername)));

        onView(withId(R.id.profile_edit_user_button)).perform(click());
        sleep();

        intended(hasComponent(EditUserActivity.class.getName()), times(2));

        onView(withId(R.id.change_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());
        onView(withId(R.id.edit_user_submit)).perform(click());
        sleep();

        Intents.release();
        activityScenarioRule.getScenario().close();
    }




    //@Test
    public void testInitialState_overallStatsVisible() {
        onView(withId(R.id.categoryText)).check(matches(withText("Overall Stats")));
        onView(withId(R.id.cardView)).check(matches(isDisplayed()));
        onView(withId(R.id.cardView2)).check(matches(isDisplayed()));
    }

    //@Test
    public void testClothesStatsButton_clickDisplaysFragmentAndHidesCards() {
        onView(withId(R.id.clothesButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Clothes Stats")));
        onView(withId(R.id.cardView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.cardView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    //@Test
    public void testOutfitStatsButton_clickDisplaysFragmentAndHidesCards() {
        onView(withId(R.id.outfitButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Outfit Stats")));
        onView(withId(R.id.cardView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.cardView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    //@Test
    public void testOverallStatsButton_clickShowsCardsAgain() {
        // Simulate clicking clothes stats first
        onView(withId(R.id.clothesButton)).perform(click());
        onView(withId(R.id.overallButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Overall Stats")));
        onView(withId(R.id.cardView)).check(matches(isDisplayed()));
        onView(withId(R.id.cardView2)).check(matches(isDisplayed()));
    }

    //@Test
    public void testBackButton_redirectsToMainActivity() {
        onView(withId(R.id.backButton)).perform(click());

        // You can add an intent verification or view check here, depending on the MainActivity layout
        // For example, check that MainActivity's toolbar is displayed
        //onView(withId(R.id.main_toolbar)).check(matches(isDisplayed()));
    }



    public static void deleteAccount(){
        Intents.init();

        ActivityScenario.launch(MainActivity.class);
        //intended(hasComponent(MainActivity.class.getName()));

        onView(withId(R.id.navigation_profile)).perform(click());
        sleep();

        onView(withId(R.id.profile_delete_user_button)).perform(click());
        sleep();
        onView(withId(R.id.delete_user_yes_button)).perform(click());

    }
    private void performLogin(){
        Intents.init();
        //Start a new activity scenario at the main class
        ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.navigation_profile)).perform(click());

        //Check if the logout button is clickable first!
        onView(withId(R.id.profile_logout_button)).check(matches(isClickable()));

        //Click it
        //onView(withId(R.id.profile_logout_button)).perform(click());
        //sleep();

        //onView(withId(R.id.profile_username_text)).check(matches(withText("Guest (not logged in)")));

        // click log in
        onView(withId(R.id.profile_login_button)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));

        //Type into the login edit texts
        onView(withId(R.id.login_username_edit)).perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.login_password_edit)).perform(typeText(testPassword), closeSoftKeyboard());

        onView(withId(R.id.login_login_button)).perform(click());
        sleep();
        sleep();

        intended(allOf(
                hasComponent(MainActivity.class.getName()),
                hasExtra("USERNAME", testUsername),
                hasExtra("PASSWORD", testPassword)
        ));

    }



    private static void sleep() {
        sleep(SIMULATED_DELAY_MS);
    }
    private static void disableAnimations(){
        try{
            Runtime.getRuntime().exec(adbPath + " shell settings put global window_animation_scale 0");
            Runtime.getRuntime().exec(adbPath + " shell settings put global transition_animation_scale 0");
            Runtime.getRuntime().exec(adbPath + " shell settings put global animator_duration_scale 0");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleep(final int delay_ms) {
        try {
            Thread.sleep(delay_ms);
        } catch (InterruptedException e) {}
    }
}



