package com.example.teachingapp.models;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {
    private Long id;
    private String subject;
    private Teacher teacher;
    private Set<Lesson> lessons;
    private Set<Student> students;
}
