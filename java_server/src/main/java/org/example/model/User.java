package org.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Email
    @Column(name = "mail", unique = true)
    private String mail;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @Column(name = "is_student")
    private boolean isStudent;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private transient Teacher teacher;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private transient Student student;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean student) {
        isStudent = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
