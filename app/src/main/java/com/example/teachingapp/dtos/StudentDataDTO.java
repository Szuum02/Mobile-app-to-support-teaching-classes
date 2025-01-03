package com.example.teachingapp.dtos;

import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.models.Presence;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDataDTO that = (StudentDataDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(index, that.index) &&
                Objects.equals(presence, that.presence) &&
                Objects.equals(todayPoints, that.todayPoints) &&
                Objects.equals(allPoints, that.allPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, index, presence, todayPoints, allPoints);
    }
}
