package com.example.teachingapp.teacher;

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


import android.view.Gravity;
import android.widget.TextView;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ActivityHistoryTask;
import com.example.teachingapp.Teacher.ActivityHistory;
import com.example.teachingapp.Teacher.GenerateReport;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.dtos.ActivityDTO;
import com.example.teachingapp.dtos.StudentHistoryDTO;
import com.example.teachingapp.retrofit.RetrofitService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ActivityHistoryTest {

    @Rule
    public ActivityScenarioRule<ActivityHistory> activityScenarioRule =
            new ActivityScenarioRule<>(ActivityHistory.class);

    @Test
    public void testButtonPresenceNavigation() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.calendar_button)).check(matches(isDisplayed()));

        onView(withId(R.id.calendar_button)).perform(click());

        intended(hasComponent(PresenceHistory.class.getName()));

        Intents.release();

    }

    @Test
    public void testButtonReportNavigation() {
        Intents.init();

        onView(withId(R.id.report_button)).check(matches(isDisplayed()));

        onView(withId(R.id.report_button)).perform(click());

        intended(hasComponent(GenerateReport.class.getName()));

        Intents.release();
    }

    @Test
    public void testStudentHistoryDataDisplayed() {
        Intents.init();

        onView(withId(R.id.description_texView)).check(matches(isDisplayed()));

        Intents.release();

    }

    @Test
    public void testSetUpLayout_studentHistoryDTOWithNotEmptyActivityDTOList() {
        StudentHistoryDTO studentHistoryDTO = new StudentHistoryDTO(
                "Jan",
                "Kowal",
                100,
                List.of(
                        new ActivityDTO("12.12.2012:10:10:10", 1),
                        new ActivityDTO("12.12.2012:10:10:10", 1),
                        new ActivityDTO("12.12.2012:10:10:10", 1)
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(
                    activity, 1L, 1L, new RetrofitService());

            activityHistoryTask.setUpLayout(studentHistoryDTO);

            assertThat(activityHistoryTask.getDescriptionTexView().getText(), is("Jan Kowal 100"));
            assertThat(activityHistoryTask.getLinearLayout().getChildCount(), is(3));
        });
    }

    @Test
    public void testSetUpLayout_studentHistoryDTOWithEmptyActivityDTOList() {
        StudentHistoryDTO studentHistoryDTO = new StudentHistoryDTO(
                "Jan",
                "Kowal",
                100,
                List.of()
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(
                    activity, 1L, 1L, new RetrofitService());

            activityHistoryTask.setUpLayout(studentHistoryDTO);

            assertThat(activityHistoryTask.getDescriptionTexView().getText(), is("Jan Kowal 100"));
            assertThat(activityHistoryTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testGenerateDateTexView() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(
                    activity, 1L, 1L, new RetrofitService());

            ActivityDTO activityDTO = new ActivityDTO("12.12.2012:10:10:10", 5);

            TextView textView = activityHistoryTask.generateDateTexView(activityDTO);

            assertEquals("12.12.2012", textView.getText().toString());
            assertEquals(Gravity.CENTER, textView.getGravity());
            assertEquals( 39.375, textView.getTextSize(), 0.1);
        });
    }

    @Test
    public void testGenerateTimeTexView() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(
                    activity, 1L, 1L, new RetrofitService());
            ActivityDTO activityDTO = new ActivityDTO("12.12.2012:10:10:10", 5);

            TextView textView = activityHistoryTask.generateTimeTexView(activityDTO);

            assertEquals("10:10:10", textView.getText().toString());
            assertEquals(Gravity.CENTER, textView.getGravity());
            assertEquals(39.375, textView.getTextSize(), 0.1);
        });
    }

    @Test
    public void testGeneratePointsTexView() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(
                    activity, 1L, 1L, new RetrofitService());
            ActivityDTO activityDTO = new ActivityDTO("12.12.2012:10:10:10", 5);

            TextView textView = activityHistoryTask.generatePointsTexView(activityDTO);

            assertEquals("5", textView.getText().toString());
            assertEquals(Gravity.CENTER, textView.getGravity());
            assertEquals(39.375, textView.getTextSize(), 0.1);
        });
    }
}
