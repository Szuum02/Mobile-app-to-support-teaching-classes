package org.example.reopsitory;

import jakarta.transaction.Transactional;
import org.example.model.Lesson;
import org.example.model.Presence;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.xml.crypto.Data;
import java.time.LocalDate;

import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    @Query("delete from Presence p where p.id = ?1 and p.date = ?2")
    public Presence removeByStudentIdAndDate(long studentId, Data date);

    @Modifying
    @Transactional
    @Query("delete from Presence p where p.lesson = ?1 and p.student = ?2")
    public void deletePresenceByLessonAndStudent(Lesson lesson, Student student);

    @Query("select p.lesson.date, p.presenceType from Presence p where p.student.id = ?1 and p.lesson.group.id = ?2 " +
            "order by p.lesson.date")
    List<Object[]> getStudentPresence(long studentId, long groupId);
}
