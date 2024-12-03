package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRankingDTO {
    private final Long id;
    private final String nick;
    private final Long totalPoints;
    private Long todayPoints;
    private final Boolean showInRanking;


    public ActivityRankingDTO(Long id, String nick, Boolean showInRanking, long totalPoints, Long todayPoints) {
        this.id = id;
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
        this.todayPoints = todayPoints;
    }

    public ActivityRankingDTO(Long id, String nick, Boolean showInRanking, long totalPoints) {
        this.id = id;
        this.nick = nick;
        this.showInRanking = showInRanking;
        this.totalPoints = totalPoints;
    }
}
