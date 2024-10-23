package com.example.teachingapp.models;

import com.example.teachingapp.enums.PresenceType;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Presence {
    private Long id;
    private LocalDateTime date;
    private PresenceType presenceType;
    private Student student;
    private Lesson lesson;
}
