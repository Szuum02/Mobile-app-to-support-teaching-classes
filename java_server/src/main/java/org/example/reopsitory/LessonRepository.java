package org.example.reopsitory;

import org.example.model.Group;
import org.example.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    public Lesson findLessonByGroupAndDate(Group group, Date date);

    Lesson findById(long lessonId);

    @Query("select s.id, s.name, s.lastname, s.index from Lesson l inner join l.group g inner join g.students s " +
            "where l.id = ?1 order by s.lastname")
    List<Object[]> findStudentsByLesson(long lessonId);
}
