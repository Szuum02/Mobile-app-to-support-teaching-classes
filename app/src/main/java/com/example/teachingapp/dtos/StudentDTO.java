package com.example.teachingapp.dtos;

import java.util.List;
import java.util.Map;

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
    private Map<String, List<LessonDTO>> lessons;
    private final Boolean showInRanking;


    public StudentDTO(Long id, String name, String lastname, Integer index, String nick, Boolean showInRanking) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
        this.nick = nick;
        this.showInRanking = showInRanking;
    }
}
