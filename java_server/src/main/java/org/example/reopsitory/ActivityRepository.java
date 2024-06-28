package org.example.reopsitory;

import org.example.model.Activity;
import org.example.model.Lesson;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select sum(a.points) from Activity a where a.student = ?1 and a.lesson = ?2")
    Integer getStudentsPointsInLesson(Student student, Lesson lesson);

    @Query("select s.nick, sum(a.points) as total_sum from Activity a inner join a.student s " +
            "where a.lesson.group.subject = ?1 group by s.id order by total_sum desc")
    List<Object[]> getRanking(String subject);
}
