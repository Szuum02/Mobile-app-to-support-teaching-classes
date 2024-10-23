package com.example.teachingapp.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDTO {
    private final LocalDateTime date;
    private final int points;

    public ActivityDTO(LocalDateTime date, int points) {
        this.date = date;
        this.points = points;
    }
}
