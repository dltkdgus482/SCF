package com.scf.battle.domain.dto.Problem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProblemAnswer {

    private Integer answerId;
    private Long problemId;
    private Integer blankPosition;
    private ProblemChoice correctChoice;
    private String correctAnswerText;
}
