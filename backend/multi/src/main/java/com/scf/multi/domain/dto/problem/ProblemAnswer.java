package com.scf.multi.domain.dto.problem;

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
