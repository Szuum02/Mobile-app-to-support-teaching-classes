package com.example.teachingapp.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentPresenceHistoryDTO {
    private final String name;
    private final String lastname;
    private final Integer index;
    private List<PresenceDTO> presences;

    public StudentPresenceHistoryDTO(String name, String lastname, Integer index, List<PresenceDTO> presences) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.presences = presences;
    }
}
