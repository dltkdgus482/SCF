package com.scf.multi.domain.dto.problem;

import java.util.List;
import lombok.Builder;
import lombok.Data;

public class ProblemResponse {

    @Data
    @Builder
    public static class ListDTO {

        private Long problemId;
        private String title;
        private ProblemType problemType;
        private String category;
        private Integer difficulty;
        private ProblemContent problemContent;
        private List<ProblemChoice> problemChoices;
    }
}
