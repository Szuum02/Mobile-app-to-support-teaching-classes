package com.example.teachingapp.teacher;

import static android.content.Context.MODE_PRIVATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Teacher.AllGroups;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.TeacherDTO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class AllGroupsTest {

    @Rule
    public ActivityScenarioRule<AllGroups> activityScenarioRule =
            new ActivityScenarioRule<>(AllGroups.class);

    @Test
    public void testShowChosenDateClasses_nonEmptyLessonList() {
        String dateString = "12.12.2012:10:10:10";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        TeacherDTO teacherDTO = new TeacherDTO(
                1L,
                "Jan",
                "Kowal"
        );
        teacherDTO.setLessons(
                Map.of(
                    "12.12.2012",
                        List.of(
                                new LessonDTO(1L, date , "123", "abc", 1L, "X"),
                                new LessonDTO(2L, date , "122", "abc2", 2L, "X2")
                        )
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(teacherDTO);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showChosenDateClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(2));
        });
    }

    @Test
    public void testShowChosenDateClasses_TeacherDTONull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(null);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showChosenDateClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testShowChosenDateClasses_emptyLessonList() {
        TeacherDTO teacherDTO = new TeacherDTO(
                1L,
                "Jan",
                "Kowal"
        );
        teacherDTO.setLessons(
                Map.of(
                        "12.12.2012",
                        List.of()
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(teacherDTO);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showChosenDateClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testShowAllClasses_nonEmptyLessonList() {
        String dateString1 = "12.12.2012:10:10:10";
        String dateString2 = "13.12.2012:10:10:10";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(dateString1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(dateString2, formatter);
        Date date1 = Date.from(dateTime1.atZone(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(dateTime2.atZone(ZoneId.systemDefault()).toInstant());


        TeacherDTO teacherDTO = new TeacherDTO(
                1L,
                "Jan",
                "Kowal"
        );
        teacherDTO.setLessons(
                Map.of(
                        "12.12.2012",
                        List.of(
                                new LessonDTO(1L, date1 , "123", "abc", 1L, "X"),
                                new LessonDTO(2L, date2 , "122", "abc2", 2L, "X2")
                        )
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(teacherDTO);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showAllClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(2));
        });
    }

    @Test
    public void testShowAllClasses_TeacherDTONull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(null);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showAllClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testShowAllClasses_emptyLessonList() {
        TeacherDTO teacherDTO = new TeacherDTO(
                1L,
                "Jan",
                "Kowal"
        );
        teacherDTO.setLessons(
                Map.of(
                        "12.12.2012",
                        List.of()
                )
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            GroupsTask groupsTask = new GroupsTask(
                    activity, activity.getSharedPreferences("Settings", MODE_PRIVATE));

            groupsTask.setTeacherDTO(teacherDTO);
            groupsTask.setChosenDate("12.12.2012");

            groupsTask.showChosenDateClasses();

            assertThat(groupsTask.getLinearLayout().getChildCount(), is(0));
        });
    }

}
