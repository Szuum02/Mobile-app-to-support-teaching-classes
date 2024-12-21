package com.example.teachingapp;

import static androidx.test.espresso.action.ViewActions.click;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.widget.Button;

import com.example.teachingapp.User._Login;
import com.example.teachingapp.User._RegistrationForm;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void properlyRender() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            assertNotNull(activity);

            assertNotNull(activity.findViewById(R.id.welcome_image));
            assertNotNull(activity.findViewById(R.id.welcome_text));
            assertNotNull(activity.findViewById(R.id.info_text));
            assertNotNull(activity.findViewById(R.id.login_button));
            assertNotNull(activity.findViewById(R.id.register_button));
        });
    }

    @Test
    public void properlyHandleLoginButtonClick() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.login_button)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(_Login.class.getName()));
    }

    @Test
    public void properlyHandleRegisterButtonClick() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(_RegistrationForm.class.getName()));
    }
}
