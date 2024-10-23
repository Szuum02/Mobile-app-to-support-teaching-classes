package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRankingDTO {
    private final String nick;
    private final long totalPoints;
    private Long todayPoints;

    public ActivityRankingDTO(String nick, long totalPoints, Long todayPoints) {
        this.nick = nick;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public ActivityRankingDTO(String nick, long totalPoints) {
        this.nick = nick;
        this.totalPoints = totalPoints;
    }
}
