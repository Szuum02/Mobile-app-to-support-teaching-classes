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
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ShowActivityGroupRanking;
import com.example.teachingapp.Student.ShowActivityTotalRanking;
import com.example.teachingapp.Student.ShowPresence;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Tasks.RankingActivityTask;
import com.example.teachingapp.Tasks.ShowPresenceTask;
import com.example.teachingapp.dtos.ActivityRankingDTO;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.StudentPresenceHistoryDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.RetrofitService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(AndroidJUnit4.class)
public class ShowPresenceTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<ShowPresence> activityScenarioRule =
            new ActivityScenarioRule<>(ShowPresence.class);

    @Test
    public void testGroupRankingButton() {
        Intents.init();

        onView(withId(R.id.three_people_button)).check(matches(isDisplayed()));
        onView(withId(R.id.three_people_button)).perform(click());

        intended(hasComponent(ShowActivityGroupRanking.class.getName()));

        Intents.release();
    }

    @Test
    public void testTotalRankingButton() {
        Intents.init();

        onView(withId(R.id.five_people_button)).check(matches(isDisplayed()));
        onView(withId(R.id.five_people_button)).perform(click());

        intended(hasComponent(com.example.teachingapp.Student.ShowActivityTotalRanking.class.getName()));

        Intents.release();
    }

    @Test
    public void testPresenceButton() {
        Intents.init();

        onView(withId(R.id.calendar_button)).check(matches(isDisplayed()));
        onView(withId(R.id.calendar_button)).perform(click());

        intended(hasComponent(ShowPresence.class.getName()));

        Intents.release();
    }

    @Test
    public void testReturnButton() {
        Intents.init();

        onView(withId(R.id.return_button)).check(matches(isDisplayed()));
        onView(withId(R.id.return_button)).perform(click());

        intended(hasComponent(StudentMainPage.class.getName()));

        Intents.release();
    }

    @Test
    public void testPresenceApiCall() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity -> {
            ShowPresenceTask task = new ShowPresenceTask(activity, 1L, 1L, "nick", sharedPreferences);
            task.findAndShowPresences();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/presence/student/get?studentId=1&groupId=1",
                    request.getPath());
            assertEquals("GET", request.getMethod());
        });
    }

    @Test
    public void testPresenceTable() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        StudentPresenceHistoryDTO presenceHistory = getPresencesHistory();
        activityScenarioRule.getScenario().onActivity(activity -> {
            ShowPresenceTask task = new ShowPresenceTask(activity, 1L, 1L, "nick", sharedPreferences);
            task.showPresences(presenceHistory);

            LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);

            assertThat(linearLayout.getChildCount(), is(4));
            testRow((LinearLayout) linearLayout.getChildAt(0), "2024-12-4", "Obecność");
            testRow((LinearLayout) linearLayout.getChildAt(1), "2024-12-3", "Usprawiedliwienie");
            testRow((LinearLayout) linearLayout.getChildAt(2), "2024-12-2", "Spóźnienie");
            testRow((LinearLayout) linearLayout.getChildAt(3), "2024-12-1", "Nieobecność");
        });
    }

    private StudentPresenceHistoryDTO getPresencesHistory() {
        List<PresenceDTO> presenceDTOS = List.of(
                new PresenceDTO("2024-12-4T15:00:00", PresenceType.O),
                new PresenceDTO("2024-12-3T15:00:00", PresenceType.U),
                new PresenceDTO("2024-12-2T15:00:00", PresenceType.S),
                new PresenceDTO("2024-12-1T15:00:00", PresenceType.N)
        );

        return new StudentPresenceHistoryDTO("name", "lastname", 1, presenceDTOS);
    }

    private void testRow(LinearLayout row, String date, String presence) {
        assertThat(row.getChildCount(), is(2));
        assertThat(row.getGravity(), is(Gravity.CENTER));
        assertThat(((TextView) row.getChildAt(0)).getText(), is(date));
        assertThat(((TextView) row.getChildAt(1)).getText(), is(presence));
        assertThat(((TextView) row.getChildAt(0)).getGravity(), is(Gravity.CENTER));
        assertThat(((TextView) row.getChildAt(1)).getGravity(), is(Gravity.CENTER));
    }
}
