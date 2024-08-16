package com.scf.problem.domain.model;

import com.scf.problem.domain.dto.ProblemRequest;
import jakarta.persistence.Column;
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
public class ProblemChoice {

    @Builder
    public ProblemChoice(String choiceText, ProblemInfo problemInfo) {
        this.choiceText = choiceText;
        this.problemInfo = problemInfo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choiceId;

    @Column(nullable = false)
    private String choiceText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private ProblemInfo problemInfo;

    public static ProblemChoice create(ProblemRequest.ProblemChoiceDTO problemChoice, ProblemInfo problem) {
        return ProblemChoice.builder()
            .choiceText(problemChoice.getChoiceText())
            .problemInfo(problem)
            .build();
    }
}
