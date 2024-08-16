package com.scf.battle.domain.dto.Problem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProblemContent {

    private Long problemId;
    private String content;
    private Integer numberOfBlanks;
}
