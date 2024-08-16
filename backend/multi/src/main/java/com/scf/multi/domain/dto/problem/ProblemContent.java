package com.scf.multi.domain.dto.problem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProblemContent {

    private Long problemId;
    private String content;
    private Integer numberOfBlank;
}
