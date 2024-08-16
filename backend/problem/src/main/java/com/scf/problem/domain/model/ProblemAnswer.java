package com.scf.problem.domain.model;

import com.scf.problem.domain.dto.ProblemRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemAnswer {

    @Builder
    public ProblemAnswer(Integer blankPosition, ProblemChoice correctChoice, String correctAnswerText, ProblemInfo problemInfo) {
        this.blankPosition = blankPosition;
        this.correctChoice = correctChoice;
        this.correctAnswerText = correctAnswerText;
        this.problemInfo = problemInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    private Integer blankPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correct_choice_id")
    private ProblemChoice correctChoice;

    private String correctAnswerText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private ProblemInfo problemInfo;

    public static ProblemAnswer create(ProblemRequest.ProblemAnswerDTO problemAnswerInfo, ProblemInfo problem, int idx) {

        if (problem.getProblemChoices().isEmpty()) {
            return ProblemAnswer.builder()
                .blankPosition(problemAnswerInfo.getBlankPosition())
                .correctAnswerText(problemAnswerInfo.getCorrectAnswerText())
                .problemInfo(problem)
                .build();
        }

        return ProblemAnswer.builder()
            .blankPosition(problemAnswerInfo.getBlankPosition())
            .correctChoice(problem.getProblemChoices().get(idx))
            .correctAnswerText(problemAnswerInfo.getCorrectAnswerText())
            .problemInfo(problem)
            .build();
    }
}