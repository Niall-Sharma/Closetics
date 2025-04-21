package com.example.closetics;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.appcompat.widget.SearchView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringEndsWith.endsWith;

import android.support.annotation.NonNull;
import android.view.View;

import com.example.closetics.follow.FollowActivity;
import com.example.closetics.follow.FollowManager;
import com.google.android.material.tabs.TabLayout;

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
     * First, logs out of current user account.
     * Second, creates a new user account.
     * Then, deletes newly created account.
     */
    @Test
    public void deleteUserAndSignUpTest(){
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


    /**
     * First, logs out of current user account.
     * Second, creates a new user account.
     * Then, deletes newly created account.
     *
     * Note: Not working because of SearchView >:(
     */
    //@Test
    public void searchUsersTest(){
        Intents.init();

        String testUsername = "user1";
        long testId = 1;

        // set a fake user
        activityScenarioRule.getScenario().onActivity(activity -> {
            UserManager.saveUsername(activity.getApplicationContext(), testUsername);
            UserManager.saveUserID(activity.getApplicationContext(), testId);
        });

        // open recommendations fragment
        onView(withId(R.id.navigation_recommendations)).perform(click());
        sleep();

        onView(withId(R.id.rec_back_button)).check(matches(not(isDisplayed())));

        // open search
        onView(withId(R.id.rec_search)).perform(click());
        //onData(allOf(is(instanceOf(SearchView.class)))).perform(click());
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            activity.findViewById(R.id.rec_search).performClick();
//        });
        sleep(2000);
        onView(withId(R.id.rec_search)).perform(submitSearchViewText("a"));
        onView(withId(R.id.rec_search)).perform(submitSearchViewText(""));
        sleep(2000);
//        onView(withId(R.id.rec_search)).perform(typeText("a"));
//        onView(withId(R.id.rec_search)).perform(clearText());

        onView(withId(R.id.rec_back_button)).check(matches(isDisplayed()));

        // search for 'test'
        onView(withId(R.id.rec_search)).perform(typeText("test"));
        sleep();

        onData(allOf(is(instanceOf(String.class)), is("testAshten"))).check(matches(isDisplayed()));

        // search for 'user'
        onView(withId(R.id.rec_search)).perform(replaceText("user"));
        sleep();

        onData(allOf(is(instanceOf(String.class)), is("user1"))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)), is("user3"))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)), is("user6"))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)), is("user9"))).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)), is("user10"))).check(matches(isDisplayed()));

        onData(allOf(is(instanceOf(String.class)), is("user11"))).check(matches(not(isDisplayed())));
        onData(allOf(is(instanceOf(String.class)), is("testAshten"))).check(matches(not(isDisplayed())));

        // click on 'user3'
        onData(allOf(is(instanceOf(String.class)), is("user3"))).perform(click());
        sleep();

        intended(hasComponent(PublicProfileActivity.class.getName()));

        onView(withId(R.id.public_profile_username_text)).check(matches(withText("user3")));

        // go back to search
        pressBack();

        // check that 'user3' is still displayed
        onData(allOf(is(instanceOf(String.class)), is("user3"))).check(matches(isDisplayed()));

        // close search
        onView(withText(R.id.rec_back_button)).perform(click());

        onView(withId(R.id.rec_back_button)).check(matches(not(isDisplayed())));

        Intents.release();
    }


    /**
     * Opens followers and following pages.
     * Goes to several people's public profiles.
     */
    @Test
    public void publicProfileAndFollowTest() {
        Intents.init();

        String testUsername = "user1";
        long testId = 1;

        // set a fake user
        activityScenarioRule.getScenario().onActivity(activity -> {
            UserManager.saveUsername(activity.getApplicationContext(), testUsername);
            UserManager.saveUserID(activity.getApplicationContext(), testId);
        });

        // open profile fragment
        onView(withId(R.id.navigation_profile)).perform(click());

        onView(withId(R.id.profile_username_text)).check(matches(withText(testUsername)));

        // open following page
        onView(withId(R.id.profile_following_button)).perform(click());
        sleep();

        intended(allOf(
                hasComponent(FollowActivity.class.getName()),
                hasExtra("IS_FOLLOWING", true)
        ));

//        onView(withId(R.id.follow_viewPager2)).check(matches(atPosition()))
//        onView(withId(R.id.follow_viewPager2)).check(actionOnItem(withText("testAshten"), matches(isDisplayed())));
        onView(withText("testAshten")).check(matches(isDisplayed()));
        //onData(allOf(is(instanceOf(String.class)), is("testAshten"))).check(matches(isDisplayed()));

        pressBack();

        //intended(hasComponent(MainActivity.class.getName()));

        // open followers page
        onView(withId(R.id.profile_followers_button)).perform(click());
        sleep();

        intended(allOf(
                hasComponent(FollowActivity.class.getName()),
                hasExtra("IS_FOLLOWING", false)
        ));

        //onView(withText("No followers")).check(matches(isDisplayed()));
        onView(withText("user2")).check(matches(isDisplayed()));
        //onData(allOf(is(instanceOf(String.class)), is("user2"))).check(matches(isDisplayed()));

        // switch to following tab
        onView(withId(R.id.follow_tab_layout)).perform(selectTabAtPosition(0));
        sleep();

        // open public profile
        onView(withText("testAshten")).perform(click());
        //onData(allOf(is(instanceOf(String.class)), is("testAshten"))).perform(click());
        sleep();

        intended(hasComponent(PublicProfileActivity.class.getName()));

        onView(withId(R.id.public_profile_username_text)).check(matches(withText("testAshten")));

        onView(withId(R.id.public_profile_following_button)).perform(click());
        sleep();

        // add follow check here

        // open another public profile
        onView(withText("user7")).perform(click());
        //onData(allOf(is(instanceOf(String.class)), is("user7"))).perform(click());
        sleep();

        onView(withId(R.id.public_profile_username_text)).check(matches(withText("user7")));

        onView(withId(R.id.public_profile_following_button)).perform(click());
        sleep();

        // open my own profile again
        onView(first(withText("user1"))).perform(click());
        //onData(allOf(is(instanceOf(String.class)), is("user1"))).perform(click());
        sleep();

        intended(hasComponent(MainActivity.class.getName()));

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

    private static ViewAction typeSearchViewText(final String text) {
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }
        };
    }

    private static ViewAction submitSearchViewText(final String text) {
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,true);
            }
        };
    }

    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }

    private <T> Matcher<T> first(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }

}

