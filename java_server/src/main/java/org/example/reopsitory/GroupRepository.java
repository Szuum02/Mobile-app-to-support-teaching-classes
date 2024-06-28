package org.example.reopsitory;

import org.example.model.Group;
import org.example.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select s.id, s.name, s.lastname, s.index from Group g inner join g.students s where g.id = ?1" +
            " order by s.lastname")
    public List<Object[]> getStudentsByGroup(Long groupId);

    public Group findById(long id);

    @Query("select l.id, l.topic from Group g inner join g.lessons l where g.id = ?1 order by l.date")
    public List<Object[]> getLessons(long groupId);
}
