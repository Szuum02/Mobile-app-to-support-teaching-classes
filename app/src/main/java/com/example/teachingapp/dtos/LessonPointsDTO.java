package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonPointsDTO {
    private final Long studentId;
    private final long points;

    public LessonPointsDTO(Long studentId, long points) {
        this.studentId = studentId;
        this.points = points;
    }

}
