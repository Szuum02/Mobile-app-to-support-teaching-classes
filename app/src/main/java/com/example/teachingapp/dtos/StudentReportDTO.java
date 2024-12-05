package com.example.teachingapp.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentReportDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<PresenceDTO> presences;
    private final Long totalPoints;

    public StudentReportDTO(String name, String lastname, Integer index, Long totalPoints) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.totalPoints = totalPoints;
    }
}
