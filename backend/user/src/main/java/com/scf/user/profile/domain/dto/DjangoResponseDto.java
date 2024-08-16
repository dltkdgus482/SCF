package com.scf.user.profile.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DjangoResponseDto {

    private int averageRank;

    private int solvedCount;

    private List<solvedProblemDto> list;


    @Data
    @Builder
    public static class solvedProblemDto {

        private boolean isCorrect;

        private String category;

        private int difficulty;
    }

}
