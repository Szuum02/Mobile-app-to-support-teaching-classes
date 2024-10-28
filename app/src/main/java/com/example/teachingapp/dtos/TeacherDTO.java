package com.example.teachingapp.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDTO {
    private final Long id;
    private final String name;
    private final String lastname;
    private Map<String, List<LessonDTO>> lessons;

    public TeacherDTO(Long id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
    }
}
