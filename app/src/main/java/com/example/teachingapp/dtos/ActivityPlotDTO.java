package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityPlotDTO {
    private final String topic;
    private final long points;

    public ActivityPlotDTO(String topic, long points) {
        this.topic = topic;
        this.points = points;
    }
}
