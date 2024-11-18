package com.example.teachingapp.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentDTO {
    private final Long id;
    private final String name;
    private final String lastname;
    private final Integer index;
    private final String nick;
    private List<GroupDTO> groups;

    public StudentDTO(Long id, String name, String lastname, Integer index, String nick) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.nick = nick;
    }
}
