package com.example.teachingapp.student;

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

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Student._StudentRegistration;
import com.example.teachingapp.User._RegistrationName;
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

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(AndroidJUnit4.class)
public class StudentRegistrationTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<_StudentRegistration> activityScenarioRule =
            new ActivityScenarioRule<>(_StudentRegistration.class);

    @Test
    public void testReturnButtonClick() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        Intents.init();

        onView(withId(R.id.return_button)).check(matches(isDisplayed()));
        onView(withId(R.id.return_button)).perform(click());

        intended(hasComponent(_RegistrationName.class.getName()));

        Intents.release();
    }

    @Test
    public void testRegisterButtonClick() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity -> {
            setUpData(activity);

            Button button = activity.findViewById(R.id.next_button);
            button.performClick();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
                assertEquals(
                        "/user/checkUniqueValues?mail=mail@example.com&index=123456&nick=nick",
                        URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.toString()));
            } catch (InterruptedException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            assertEquals("GET", request.getMethod());
        });
    }

    @Test
    public void testRegisterStudent() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.registerStudent("mail@example.com", "password", 123456, "nick");

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
                assertEquals(
                        "/user/addStudent?nick=nick&index=123456&mail=mail@example.com&password=password",
                        URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.toString()));
            } catch (InterruptedException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            assertEquals("POST", request.getMethod());
        });
    }

    @Test
    public void testEmailValidation() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            EditText mailText = activity.findViewById(R.id.login);

            mailText.setText("wrong.com");
            assertFalse(activity.validateEmail(mailText));
            assertThat(mailText.getError(), is("Błędny email"));
            mailText.setError(null);

            mailText.setText("wrong@example");
            assertFalse(activity.validateEmail(mailText));
            assertThat(mailText.getError(), is("Błędny email"));
            mailText.setError(null);

            mailText.setText("wrong@wrong@example");
            assertFalse(activity.validateEmail(mailText));
            assertThat(mailText.getError(), is("Błędny email"));
            mailText.setError(null);

            mailText.setText("correct@example.com");
            assertTrue(activity.validateEmail(mailText));
            assertNull(mailText.getError());
        });
    }

    @Test
    public void testConfirmPasswordValidation() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            EditText passwordText = activity.findViewById(R.id.password);
            EditText confirmPasswordText = activity.findViewById(R.id.confirmPassword);

            passwordText.setText("password");
            confirmPasswordText.setText("otherPassword");
            assertFalse(activity.validateConfirmPassword(passwordText, confirmPasswordText));
            assertThat(confirmPasswordText.getError(), is("Hasła są różne"));
            confirmPasswordText.setError(null);

            confirmPasswordText.setText("password");
            assertTrue(activity.validateConfirmPassword(passwordText, confirmPasswordText));
            assertNull(confirmPasswordText.getError());
        });
    }

    @Test
    public void testIndexValidation() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            EditText indexText = activity.findViewById(R.id.index);

            indexText.setText("text");
            assertFalse(activity.validateIndex(indexText));
            assertThat(indexText.getError(), is("Album powinien składać się z 6 cyfr"));
            indexText.setError(null);

            indexText.setText("12345");
            assertFalse(activity.validateIndex(indexText));
            assertThat(indexText.getError(), is("Album powinien składać się z 6 cyfr"));
            indexText.setError(null);

            indexText.setText("-123456");
            assertFalse(activity.validateIndex(indexText));
            assertThat(indexText.getError(), is("Album powinien składać się z 6 cyfr"));
            indexText.setError(null);

            indexText.setText("123456");
            assertTrue(activity.validateIndex(indexText));
            assertNull(indexText.getError());
        });
    }

    private void setUpData(_StudentRegistration activity) {
        EditText mailText = activity.findViewById(R.id.login);
        mailText.setText("mail@example.com");
        EditText passwordText = activity.findViewById(R.id.password);
        passwordText.setText("password");
        EditText confirmPasswordText = activity.findViewById(R.id.confirmPassword);
        confirmPasswordText.setText("password");
        EditText nickText = activity.findViewById(R.id.nick);
        nickText.setText("nick");
        EditText indexText = activity.findViewById(R.id.index);
        indexText.setText("123456");
    }
}
