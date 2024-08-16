package com.scf.user.profile.domain.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

// 문제 서버에서 정보를 받아오는 dto 정의
@Data
@Builder
public class ProblemResponseDto {

    private Long problemId;

    private String title;

    private ProblemType problemType;

    private String category;

    private Integer difficulty;

    private ProblemContentDTO problemContent;

    private List<ProblemChoiceDTO> problemChoices;

    private List<ProblemAnswerDTO> problemAnswers;

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
