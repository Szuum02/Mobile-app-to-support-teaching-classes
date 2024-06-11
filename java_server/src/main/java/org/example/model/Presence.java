package org.example.model;

import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "presences")
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "presence_id")
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "presence_type", columnDefinition = "VARCHAR(1)")
    @NotNull
    private PresenceType presenceType;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PresenceType getPresenceType() {
        return presenceType;
    }

    public void setPresenceType(PresenceType presenceType) {
        this.presenceType = presenceType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
