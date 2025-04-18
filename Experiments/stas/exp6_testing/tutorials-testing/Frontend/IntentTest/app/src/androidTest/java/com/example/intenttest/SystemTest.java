package com.example.intenttest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SystemTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLogin(){
        Intents.init();
        String testString = "text to test";

        onView(withId(R.id.edt)).perform(typeText(testString), closeSoftKeyboard());    // Type in testString
        onView(withId(R.id.toSecondBtn)).perform(click());  // Click on To Second Activity button

        intended(allOf( // Check if landed to the second activity and verify extras
                hasComponent(SecondActivity.class.getName()),
                hasExtra("text", testString)
        ));

        onView(withId(R.id.toMainBtn)).perform(click());        // Click on To Main Activity button
        intended(hasComponent(MainActivity.class.getName()));   // Check if landed to the second activity

        Intents.release();
    }

}