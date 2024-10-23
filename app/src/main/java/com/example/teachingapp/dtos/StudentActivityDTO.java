package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentActivityDTO {
    private final String name;
    private final String lastname;
    private final long totalPoints;
    private Long todayPoints;

    public StudentActivityDTO(String name, String lastname, long totalPoints, Long todayPoints) {
        this.name = name;
        this.lastname = lastname;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

}
