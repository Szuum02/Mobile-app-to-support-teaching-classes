package org.example.reopsitory;

import org.example.model.Activity;
import org.example.model.Lesson;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select sum(a.points) from Activity a where a.student = ?1 and a.lesson = ?2")
    Integer getStudentsPointsInLesson(Student student, Lesson lesson);
}
