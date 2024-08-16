package com.scf.multi.domain.dto.problem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProblemChoice {

    private Integer choiceId;
    private Long problemId;
    private String choiceText;
}
