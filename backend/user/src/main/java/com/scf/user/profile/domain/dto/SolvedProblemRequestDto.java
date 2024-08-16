package com.scf.user.profile.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolvedProblemRequestDto {

    private Long memberId;
    private int problemId;
    private String choice;
    private String choiceText;
    private boolean isCorrect;

}
