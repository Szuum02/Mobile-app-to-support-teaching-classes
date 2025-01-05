package com.example.teachingapp.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.SettingsActivityStudent;
import com.example.teachingapp.Tasks.SettingsTaskStudent;
import com.example.teachingapp.retrofit.RetrofitService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityStudentTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<SettingsActivityStudent> activityScenarioRule =
            new ActivityScenarioRule<>(SettingsActivityStudent.class);

    @Test
    public void testReturnButton() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            SettingsTaskStudent taskStudent = new SettingsTaskStudent(activity, 1L);
            taskStudent.startTask();

            activity.findViewById(R.id.return_button).performClick();

            assertTrue(activity.isFinishing());
        });
    }

    @Test
    public void testYesButtonClick() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            SettingsTaskStudent task = new SettingsTaskStudent(activity, 1L);
            task.startTask();

            activity.findViewById(R.id.yes_button).performClick();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/student/changeShowInRanking?studentId=1&showInRanking=true",
                    request.getPath());
            assertEquals("POST", request.getMethod());
        });
    }

    @Test
    public void testNoButtonClick() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            SettingsTaskStudent task = new SettingsTaskStudent(activity, 1L);
            task.startTask();

            activity.findViewById(R.id.no_button).performClick();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/student/changeShowInRanking?studentId=1&showInRanking=false",
                    request.getPath());
            assertEquals("POST", request.getMethod());
        });
    }
}
