package com.scf.problem.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProblemAnswerDTO {

    private Integer answerId;

    private Integer problemId;

    private Integer blankPosition;

    private ProblemChoiceDTO correctChoice;

    private String correctAnswerText;
}
