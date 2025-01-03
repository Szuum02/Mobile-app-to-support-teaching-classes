package com.example.teachingapp.user;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.teachingapp.MainActivity;
import com.example.teachingapp.R;
import com.example.teachingapp.User._RegistrationForm;
import com.example.teachingapp.User._RegistrationName;
import com.example.teachingapp.retrofit.RetrofitService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class RegistrationFormTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<_RegistrationForm> activityScenarioRule =
            new ActivityScenarioRule<>(_RegistrationForm.class);

    @Test
    public void testTeacherButton() {
        Intents.init();

        onView(withId(R.id.teacher_button)).check(matches(isDisplayed()));

        onView(withId(R.id.teacher_button)).perform(click());

        intended(hasComponent(_RegistrationName.class.getName()));

        Intents.release();
    }

    @Test
    public void testStudentButton() {
        Intents.init();

        onView(withId(R.id.student_button)).check(matches(isDisplayed()));

        onView(withId(R.id.student_button)).perform(click());

        intended(hasComponent(_RegistrationName.class.getName()));

        Intents.release();
    }

    @Test
    public void testReturnButton() {
        Intents.init();

        onView(withId(R.id.return_button)).check(matches(isDisplayed()));

        onView(withId(R.id.return_button)).perform(click());

        intended(hasComponent(MainActivity.class.getName()));

        Intents.release();
    }
}
