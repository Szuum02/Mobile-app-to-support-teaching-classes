package com.example.teachingapp.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDTO {
    private final Long id;
    private final boolean student;
    public UserDTO(Long id, boolean isStudent) {
        this.id = id;
        this.student = isStudent;
    }
}
