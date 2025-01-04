package com.example.teachingapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.teachingapp.User._Login;
import com.example.teachingapp.User._RegistrationForm;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoginButtonClick() {
        Intents.init();

        onView(withId(R.id.login_button)).check(matches(isDisplayed()));

        onView(withId(R.id.login_button)).perform(click());

        intended(hasComponent(_Login.class.getName()));

        Intents.release();
    }

    @Test
    public void testRegisterButtonClick() {
        Intents.init();

        onView(withId(R.id.register_button)).check(matches(isDisplayed()));

        onView(withId(R.id.register_button)).perform(click());

        intended(hasComponent(_RegistrationForm.class.getName()));

        Intents.release();
    }
}
