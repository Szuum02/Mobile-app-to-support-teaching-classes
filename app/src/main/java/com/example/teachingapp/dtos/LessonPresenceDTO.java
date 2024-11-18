package com.example.teachingapp.dtos;

import com.example.teachingapp.enums.PresenceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonPresenceDTO {
    private final Long studentId;
    private final PresenceType presenceType;

    public LessonPresenceDTO(Long studentId, PresenceType presenceType) {
        this.studentId = studentId;
        this.presenceType = presenceType;
    }
}
