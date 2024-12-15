package com.example.teachingapp.dtos;

import com.example.teachingapp.models.Lesson;
import com.example.teachingapp.models.Student;
import com.example.teachingapp.models.Teacher;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupDTO {
    private String subjectCode;
    private Integer groupNumber;
    private String subject;
    private Set<AddLessonDTO> lessons;

    public AddGroupDTO(String subjectCode, String subject, Integer groupNumber) {
        this.subjectCode = subjectCode;
        this.subject = subject;
        this.groupNumber = groupNumber;
    }
}
