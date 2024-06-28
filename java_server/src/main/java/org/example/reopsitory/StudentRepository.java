package org.example.reopsitory;

import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    public Student findById(long id);

    @Query("SELECT g.id, g.subject FROM Student s join s.groups g WHERE s.id = ?1")
    List<Object[]> getStudentsGroup(long studentId);
}
