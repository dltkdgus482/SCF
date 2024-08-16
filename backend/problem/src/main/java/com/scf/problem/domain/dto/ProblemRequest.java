package com.scf.problem.domain.dto;

import com.scf.problem.constant.ProblemType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

public class ProblemRequest {

    @Data
    @Builder
    public static class ProblemInfoDTO {

        private String title;
        private ProblemType problemType;
        private String category;
        private Integer difficulty;
        private ProblemContentDTO problemContent;
        private List<ProblemChoiceDTO> problemChoices;
        private List<ProblemAnswerDTO> problemAnswers;
    }

    @Data
    @Builder
    public static class ProblemContentDTO {

        private String content;
        private Integer numberOfBlanks;
    }

    @Data
    @Builder
    public static class ProblemChoiceDTO {

        private Long problemId;
        private String choiceText;
    }

    @Data
    @Builder
    public static class ProblemAnswerDTO {

        private Integer blankPosition;
        private ProblemChoiceDTO correctChoice;
        private String correctAnswerText;
    }
}
