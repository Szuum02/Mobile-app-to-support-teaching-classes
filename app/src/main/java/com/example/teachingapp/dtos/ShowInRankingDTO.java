package com.example.teachingapp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowInRankingDTO {
    private final Long id;
    private final Boolean showInRanking;

    public ShowInRankingDTO(Long id, Boolean showInRanking) {
        this.id = id;
        this.showInRanking = showInRanking;
    }
}
