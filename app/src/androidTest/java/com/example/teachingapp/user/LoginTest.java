package com.example.teachingapp.user;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Tasks.ShowPresenceTask;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.User._Login;
import com.example.teachingapp.User._RegistrationForm;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.dtos.UserDTO;
import com.example.teachingapp.retrofit.RetrofitService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<_Login> activityScenarioRule =
            new ActivityScenarioRule<>(_Login.class);

    @Test
    public void testLoginButtonClick() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity -> {
            EditText mailText = activity.findViewById(R.id.mail);
            mailText.setText("mail@example.com");
            EditText passwordText = activity.findViewById(R.id.password);
            passwordText.setText("password");

            Button loginButton = activity.findViewById(R.id.next_button);
            loginButton.performClick();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
                assertEquals(
                        "/user/login?mail=mail@example.com&password=password",
                        URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.toString()));
            } catch (InterruptedException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            assertEquals("POST", request.getMethod());
        });
    }

    @Test
    public void testStudentLogin() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        UserDTO userDTO = new UserDTO(1L, true);

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.tryLogin(userDTO);

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
    }

    @Test
    public void testTeacherLogin() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        UserDTO userDTO = new UserDTO(1L, false);

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.tryLogin(userDTO);

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/teacher/login?id=1",
                    request.getPath());
            assertEquals("POST", request.getMethod());
        });
    }
}
