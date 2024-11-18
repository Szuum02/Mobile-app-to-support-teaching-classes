package com.example.teachingapp.dtos;

import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.models.Presence;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDataDTO {
    private Long id;
    private String name;
    private String lastname;
    private Long index;
    private PresenceType presence;
    private Long todayPoints;
    private Long allPoints;

    public StudentDataDTO(Long id, String name, String lastname, Long index) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.presence = PresenceType.NOT_GIVEN;
        this.todayPoints = 0L;
        this.allPoints = 0L;
    }
}
