package com.example.teachingapp.teacher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.Tasks.PresenceHistoryTeacherTask;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.StudentPresenceHistoryDTO;
import com.example.teachingapp.enums.PresenceType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PresenceHistoryTest {

    @Rule
    public ActivityScenarioRule<PresenceHistory> activityScenarioRule =
            new ActivityScenarioRule<>(PresenceHistory.class);

    @Test
    public void testSetUpLayout_notEmptyStudentDataDTOList() {
        StudentPresenceHistoryDTO studentPresenceHistoryDTO = new StudentPresenceHistoryDTO(
                "Jan",
                "Kowal",
                1000,
                List.of(
                        new PresenceDTO("11.11.2011", PresenceType.S),
                        new PresenceDTO("11.11.2011", PresenceType.S),
                        new PresenceDTO("11.11.2011", PresenceType.S)
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            PresenceHistoryTeacherTask presenceHistoryTeacherTask = new PresenceHistoryTeacherTask(
                    activity, 1L, 1L);

            presenceHistoryTeacherTask.setUpLayout(studentPresenceHistoryDTO);

            assertThat(presenceHistoryTeacherTask.getLinearLayout().getChildCount(), is(3));
        });
    }
}
