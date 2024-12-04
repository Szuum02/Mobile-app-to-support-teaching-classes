package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonPointsDTO {
    private final Long studentId;
    private final Long totalPoints;
    private final Long todayPoints;

    public LessonPointsDTO(Long studentId, long totalPoints, long todayPoints) {
        this.studentId = studentId;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

}
