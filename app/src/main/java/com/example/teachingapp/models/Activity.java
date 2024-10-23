package com.example.teachingapp.models;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Activity {
    private Integer points;
    private LocalDateTime date;
    private Student student;
    private Lesson lesson;
}
