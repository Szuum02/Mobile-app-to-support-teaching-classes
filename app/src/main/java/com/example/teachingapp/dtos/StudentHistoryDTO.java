package com.example.teachingapp.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentHistoryDTO {

    private final String name;
    private final String lastname;
    private final Integer index;
    private List<ActivityDTO> activities;

    public StudentHistoryDTO(String name, String lastname, Integer index, List<ActivityDTO> activities) {
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.activities = activities;
    }

}
