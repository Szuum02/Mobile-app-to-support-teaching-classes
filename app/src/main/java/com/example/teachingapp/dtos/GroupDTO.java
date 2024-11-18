package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupDTO {
    private final Long groupId;
    private final String subject;

    public GroupDTO(Long groupId, String subject) {
        this.groupId = groupId;
        this.subject = subject;
    }
}
