package com.example.closetics;


import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.StatisticsActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.StringEndsWith.endsWith;

import static java.util.function.Predicate.not;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

// Mock the RequestServerForService class
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time

/**
 * I will be testing the leaderboard activity for the mini assignment starting with
 * the leaderboard intent.
 */
public class AshtenSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;
    private MockWebServer mockWebServer;
    private CountingIdlingResource idlingResource;


    /*

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        // Initialize the IdlingResource
        idlingResource = new CountingIdlingResource("NetworkCall");

        // Register the IdlingResource to sync Espresso with the network calls
        IdlingRegistry.getInstance().register(idlingResource);

        mockWebServer.setDispatcher(new Dispatcher() {
            @NonNull
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();
                Log.d("request path", path);

                if (path.startsWith("getOutfit/")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{ \"id\": 6, \"price\": 49.99 }");
                } else if (path.startsWith("getUsersMostExpensiveOutfit/")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{ \"outfitName\": TestOutfit }");
                }

                return new MockResponse().setResponseCode(404);
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
        IdlingRegistry.getInstance().unregister(idlingResource);

    }

    @Test
    public void testOverallStatsTextViews_displayCorrectData() throws InterruptedException, IOException {
        //Mock get most expensive outfi

        // Mock GET response for total outfits
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[{\"outfitId\":1,\"outfitName\":\"Fit A\",\"outfitStats\":{}}," +
                        "{\"outfitId\":2,\"outfitName\":\"Fit B\",\"outfitStats\":{}}]"));

        // Mock GET response for clothing items
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[{\"clothesId\":1,\"itemName\":\"Shirt\",\"price\":30,\"clothingStats\":{}}," +
                        "{\"clothesId\":2,\"itemName\":\"Jeans\",\"price\":70,\"clothingStats\":{}}]"));

        // Mock response for most expensive outfit
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"outfitId\":1,\"totalPrice\":\"100\"}"));

        // Mock individual outfit info
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"outfitName\":\"Fit A\"}"));



        mockWebServer.start();
        // Replace base URL for tests
        MainActivity.SERVER_URL = mockWebServer.url("").toString();

        // Launch the activity
        ActivityScenario.launch(StatisticsActivity.class);

       idlingResource.increment();

        //String outfitText = TextViewTextCapture.getText(withId(R.id.totalOutfitsCount));
        //Log.d("TEST_OUTFIT_TEXT", outfitText);

        //String closetText = TextViewTextCapture.getText(withId(R.id.totalClosetCount));
        //Log.d("TEST_CLOSET_TEXT", closetText);


        // Assertions for UI
        onView(withId(R.id.expensiveOutfit)).check(matches(withText("TestOutfit")));
        //onView(withId(R.id.totalClosetCount)).check(matches(withText("100"))); // 30 + 70
        //onView(withId(R.id.clothingCount)).check(matches(withText("2")));
        //onView(withId(R.id.expensiveOutfit)).check(matches(withSubstring("Fit A")));
        //onView(withId(R.id.expensiveOutfit)).check(matches(withSubstring("Price: 100")));

        idlingResource.decrement();

    }



    public static class TextViewTextCapture {
        public static String getText(final Matcher<View> matcher) {
            final String[] textHolder = new String[1];

            onView(matcher).check((view, noViewFoundException) -> {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }

                TextView tv = (TextView) view;
                textHolder[0] = tv.getText().toString();
            });

            return textHolder[0];
        }
    }
     */
    @Before
    public void launchActivity() {
        ActivityScenario.launch(new Intent(Intent.ACTION_MAIN)
                .setClassName("com.example.closetics", "com.example.closetics.dashboard.StatisticsActivity"));
    }

    @Test
    public void testInitialState_overallStatsVisible() {
        onView(withId(R.id.categoryText)).check(matches(withText("Overall Stats")));
        onView(withId(R.id.cardView)).check(matches(isDisplayed()));
        onView(withId(R.id.cardView2)).check(matches(isDisplayed()));
    }

    @Test
    public void testClothesStatsButton_clickDisplaysFragmentAndHidesCards() {
        onView(withId(R.id.clothesButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Clothes Stats")));
        onView(withId(R.id.cardView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.cardView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testOutfitStatsButton_clickDisplaysFragmentAndHidesCards() {
        onView(withId(R.id.outfitButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Outfit Stats")));
        onView(withId(R.id.cardView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.cardView2)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testOverallStatsButton_clickShowsCardsAgain() {
        // Simulate clicking clothes stats first
        onView(withId(R.id.clothesButton)).perform(click());
        onView(withId(R.id.overallButton)).perform(click());

        onView(withId(R.id.categoryText)).check(matches(withText("Overall Stats")));
        onView(withId(R.id.cardView)).check(matches(isDisplayed()));
        onView(withId(R.id.cardView2)).check(matches(isDisplayed()));
    }

    @Test
    public void testBackButton_redirectsToMainActivity() {
        onView(withId(R.id.backButton)).perform(click());

        // You can add an intent verification or view check here, depending on the MainActivity layout
        // For example, check that MainActivity's toolbar is displayed
        //onView(withId(R.id.main_toolbar)).check(matches(isDisplayed()));
    }


}



