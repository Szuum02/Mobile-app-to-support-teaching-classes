package com.example.teachingapp.student;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.text.SpannableString;
import android.widget.TextView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.MainActivity;
import com.example.teachingapp.R;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Tasks.MainPageStudentTask;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.retrofit.RetrofitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(AndroidJUnit4.class)
public class StudentMainPageTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<StudentMainPage> activityScenarioRule =
            new ActivityScenarioRule<>(StudentMainPage.class);

    @Test
    public void testLogoutButton() {
        Intents.init();

        onView(withId(R.id.log_off_button)).check(matches(isDisplayed()));
        onView(withId(R.id.log_off_button)).perform(click());

        intended(hasComponent(MainActivity.class.getName()));

        Intents.release();
    }

    @Test
    public void testWelcomeText() throws JsonProcessingException {
        StudentDTO studentDTO = setUpData();
        Gson gson = new Gson();

        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(any(), any())).thenReturn(gson.toJson(studentDTO));

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            task.startTask();

            TextView helloTextView = activity.findViewById(R.id.welcome_texView);
            assertEquals("Witaj name lastname", helloTextView.getText());
        });
    }

    @Test
    public void testNoUpcomingLesson() {
        StudentDTO studentDTO = setUpData();
        Gson gson = new Gson();

        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(any(), any())).thenReturn(gson.toJson(studentDTO));

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            task.startTask();

            TextView subjectTextView = activity.findViewById(R.id.subject_textView);
            assertEquals("Brak najbliższych zajęć", subjectTextView.getText());
        });
    }

    @Test
    public void testUpcomingLesson() {
        StudentDTO studentDTO = setUpData();
        LocalDateTime dateTime = LocalDateTime.now();
        LessonDTO lessonDTO = new LessonDTO(1L, Date.from(dateTime.plusDays(1).atZone(ZoneId.systemDefault()).toInstant()), "classroom1", "topic1", 1L, "code1");
        studentDTO.setLessons(Map.of(
                LocalDate.now().toString(), List.of(lessonDTO)));

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(any(), any())).thenReturn(gson.toJson(studentDTO));

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            task.startTask();

            String dateString = task.getDateFromData(lessonDTO);
            SpannableString spannableString = createSpannable(dateString);
            TextView subjectTextView = activity.findViewById(R.id.subject_textView);
            assertEquals(spannableString.toString(), subjectTextView.getText().toString());
        });
    }

    @Test
    public void testGetDateFromData() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            LessonDTO lessonDTO = new LessonDTO(1L, Date.from(LocalDateTime.of(2024, 12, 1, 10, 20, 0).atZone(ZoneId.systemDefault()).toInstant()), "classroom1", "topic1", 1L, "code1");
            assertEquals("1-12-2024\n10:20", task.getDateFromData(lessonDTO));

            lessonDTO = new LessonDTO(1L, Date.from(LocalDateTime.of(2024, 2, 1, 3, 7, 0).atZone(ZoneId.systemDefault()).toInstant()), "classroom1", "topic1", 1L, "code1");
            assertEquals("1-02-2024\n03:07", task.getDateFromData(lessonDTO));
        });
    }

    @Test
    public void testAddZeroToString() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            assertEquals("01", task.addZeroToString("1"));
            assertEquals("10", task.addZeroToString("10"));
        });
    }

    @Test
    public void testFindUpcomingClassesThenReturnNull() {
        StudentDTO studentDTO = setUpData();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(any(), any())).thenReturn(gson.toJson(studentDTO));

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            assertNull(task.findUpcomingClasses());
        });
    }

    @Test
    public void testFindUpcomingClassesThenReturnClass() {
        StudentDTO studentDTO = setUpData();
        LocalDateTime dateTime = LocalDateTime.now();
        LessonDTO lessonDTO = new LessonDTO(1L, Date.from(dateTime.plusDays(1).atZone(ZoneId.systemDefault()).toInstant()), "classroom1", "topic1", 1L, "code1");
        studentDTO.setLessons(Map.of(
                LocalDate.now().toString(), List.of(lessonDTO)));

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(sharedPreferences.getString(any(), any())).thenReturn(gson.toJson(studentDTO));

        activityScenarioRule.getScenario().onActivity(activity ->  {
            MainPageStudentTask task = new MainPageStudentTask(activity, sharedPreferences);
            assertEquals(gson.toJson(lessonDTO), gson.toJson(task.findUpcomingClasses()));
        });
    }

    private StudentDTO setUpData() {
        StudentDTO studentDTO = new StudentDTO(1L, "name", "lastname", 1, "nick", true);
        studentDTO.setLessons(Map.of(
                "2024-12-12", List.of(
                        new LessonDTO(1L,  Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), "classroom1", "topic1", 1L, "code1"),
                        new LessonDTO(2L, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), "classroom2", "topic2", 2L, "code2")
                )
        ));
        return studentDTO;
    }

    private SpannableString createSpannable(String dateString) {
        return new SpannableString("topic1\n" + dateString + "\nclassroom1");
    }
}
