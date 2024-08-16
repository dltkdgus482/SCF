package com.scf.problem.domain.model;

import com.scf.problem.domain.dto.ProblemRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemContent {

    @Builder
    public ProblemContent(String content, Integer numberOfBlanks, ProblemInfo problemInfo) {
        this.content = content;
        this.numberOfBlanks = numberOfBlanks;
        this.problemInfo = problemInfo;
    }

    @Id
    private Long problemId;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Integer numberOfBlanks;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "problem_id")
    private ProblemInfo problemInfo;

    public static ProblemContent create(ProblemRequest.ProblemContentDTO problemContent, ProblemInfo problem) {

        return ProblemContent.builder()
            .content(problemContent.getContent())
            .numberOfBlanks(problemContent.getNumberOfBlanks())
            .problemInfo(problem)
            .build();
    }
}
