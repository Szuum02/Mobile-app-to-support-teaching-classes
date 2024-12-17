package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLessonDTO {
    private String date;
    private String classroom;

    public AddLessonDTO(String date, String classroom) {
        this.date = date;
        this.classroom = classroom;
    }
}
