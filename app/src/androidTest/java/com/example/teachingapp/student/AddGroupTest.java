package com.example.teachingapp.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

import android.content.SharedPreferences;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.AddGroup;
import com.example.teachingapp.Tasks.AddStudentToGroupTask;
import com.example.teachingapp.retrofit.RetrofitService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AddGroupTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<AddGroup> activityScenarioRule =
            new ActivityScenarioRule<>(AddGroup.class);

    @Test
    public void testAddButtonClick() throws InterruptedException {
        Intents.init();

        onView(withId(R.id.group_code)).perform(replaceText("code1"));
        onView(withId(R.id.add_group_button)).check(matches(isDisplayed()));
        onView(withId(R.id.add_group_button)).perform(click());

        RecordedRequest request = server.takeRequest();
        assertEquals(
                "/student/addGroup?studentId=0&groupCode=code1",
                request.getPath());
        assertEquals("POST", request.getMethod());

        Intents.release();
    }

    @Test
    public void refreshStudentLessons() throws JsonProcessingException {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        Intents.init();

        activityScenarioRule.getScenario().onActivity(activity -> {
            AddStudentToGroupTask task = new AddStudentToGroupTask(activity, 1L, sharedPreferences);
            task.refreshStudentData();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/student/login?studentId=1",
                    request.getPath());
            assertEquals("POST", request.getMethod());
        });

        Intents.release();
    }

    @After
    public void closeServer() throws IOException {
        server.close();
    }
}
