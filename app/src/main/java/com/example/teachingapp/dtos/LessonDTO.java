package com.example.teachingapp.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonDTO {
    private final Long lessonId;
    private final Date date;
    private final String classroom;
    private final String topic;
    private final Long groupId;
    private final String groupCode;

    public LessonDTO(Long lessonId, Date date, String classroom, String topic, Long groupId, String groupCode) {
        this.lessonId = lessonId;
        this.date = date;
        this.classroom = classroom;
        this.topic = topic;
        this.groupId = groupId;
        this.groupCode = groupCode;
    }

}
