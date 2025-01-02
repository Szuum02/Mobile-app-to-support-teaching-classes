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
import com.example.teachingapp.Tasks.AddStudentToGroupTask;
import com.example.teachingapp.Tasks.RankingActivityTask;
import com.example.teachingapp.dtos.ActivityRankingDTO;
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
public class ShowActivityGroupRankingTest {
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.enqueue(new MockResponse());
        server.start();
        RetrofitService.setBaseUrl(server.url("/").toString());
    }

    @Rule
    public ActivityScenarioRule<ShowActivityGroupRanking> activityScenarioRule =
            new ActivityScenarioRule<>(ShowActivityGroupRanking.class);

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

        intended(hasComponent(ShowActivityTotalRanking.class.getName()));

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
    public void testGroupRankingApiCall() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        activityScenarioRule.getScenario().onActivity(activity -> {
            RankingActivityTask task = new RankingActivityTask(activity, 1L, 1L, "nick", sharedPreferences);
            task.getGroupRanking();

            RecordedRequest request = null;
            try {
                request = server.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals(
                    "/activity/groupRanking?groupId=1",
                    request.getPath());
            assertEquals("GET", request.getMethod());
        });
    }

    @Test
    public void testGroupRankingTable() {
        SharedPreferences sharedPreferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        List<ActivityRankingDTO> activityRankingDTOS = List.of(
                new ActivityRankingDTO(1L, "nick1", true, 5L, null),
                new ActivityRankingDTO(2L, "nick2", true, 4L, 4L),
                new ActivityRankingDTO(3L, "nick3", false, 3L, 0L),
                new ActivityRankingDTO(4L, "nick4", true, 2L, 1L)
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            RankingActivityTask task = new RankingActivityTask(activity, 1L, 2L, "nick2", sharedPreferences);
            task.createGroupRanking(activityRankingDTOS);

            LinearLayout linearLayout = activity.findViewById(R.id.linearLayout);

            assertThat(linearLayout.getChildCount(), is(4));
            for (int i = 0; i < 4; i++) {
                ActivityRankingDTO activityRankingDTO = activityRankingDTOS.get(i);
                LinearLayout rowLayout = (LinearLayout) linearLayout.getChildAt(i);
                testRow(rowLayout, i + 1, activityRankingDTO.getShowInRanking(), activityRankingDTO.getTotalPoints(), activityRankingDTO.getTodayPoints());
            }
            assertThat(((ColorDrawable) linearLayout.getChildAt(1).getBackground()).getColor(), is(Color.parseColor("#8ADAB2")));
        });
    }

    private void testRow(LinearLayout row, int i, boolean showInRanking, Long totalPoints, Long todayPoints) {
        assertThat(row.getChildCount(), is(3));
        assertThat(((TextView) row.getChildAt(0)).getText(), is(showInRanking ? "nick" + i : "-"));
        assertThat(((TextView) row.getChildAt(1)).getText(), is(totalPoints.toString()));
        assertThat(((TextView) row.getChildAt(2)).getText(), is(todayPoints != null ? (todayPoints > 0 ? "+" + todayPoints : todayPoints.toString()): ""));
    }
}
