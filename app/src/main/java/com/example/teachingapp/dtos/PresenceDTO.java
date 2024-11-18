package com.example.teachingapp.dtos;

import com.example.teachingapp.enums.PresenceType;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PresenceDTO {
    private final String date;
    private final PresenceType presenceType;

    public PresenceDTO(String date, PresenceType presenceType) {
        this.date = date;
        this.presenceType = presenceType;
    }
}
