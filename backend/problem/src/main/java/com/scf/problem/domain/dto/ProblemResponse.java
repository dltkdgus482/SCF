package com.scf.problem.domain.dto;

import com.scf.problem.constant.ProblemType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

public class ProblemResponse {

    @Data
    @Builder
    public static class ProblemInfoDTO {

        private Long problemId;

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

        private Long problemId;

        private String content;

        private Integer numberOfBlanks;
    }

    @Data
    @Builder
    public static class ProblemChoiceDTO {

        private Integer choiceId;

        private Long problemId;

        private String choiceText;
    }

    @Data
    @Builder
    public static class ProblemAnswerDTO {

        private Integer answerId;

        private Long problemId;

        private Integer blankPosition;

        private ProblemChoiceDTO correctChoice;

        private String correctAnswerText;
    }
}
