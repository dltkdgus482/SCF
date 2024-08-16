package com.scf.problem.domain.model;

import com.scf.problem.constant.ProblemType;
import com.scf.problem.domain.dto.ProblemRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.parsing.Problem;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer difficulty;

    @OneToOne(mappedBy = "problemInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProblemContent problemContent;

    @OneToMany(mappedBy = "problemInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProblemChoice> problemChoices = new ArrayList<>();

    @OneToMany(mappedBy = "problemInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProblemAnswer> problemAnswers = new ArrayList<>();

    @Builder
    public ProblemInfo(String title, ProblemType problemType, String category, Integer difficulty) {
        this.title = title;
        this.problemType = problemType;
        this.category = category;
        this.difficulty = difficulty;
    }

    public void associateProblemContent(ProblemContent problemContent) {
        this.problemContent = problemContent;
    }

    public static ProblemInfo create(ProblemRequest.ProblemInfoDTO problemInfoDTO) {

        ProblemInfo problem = ProblemInfo.builder()
            .title(problemInfoDTO.getTitle())
            .problemType(problemInfoDTO.getProblemType())
            .category(problemInfoDTO.getCategory())
            .difficulty(problemInfoDTO.getDifficulty())
            .build();

        ProblemContent problemContent = ProblemContent.create(problemInfoDTO.getProblemContent(), problem);

        problem.associateProblemContent(problemContent);

        for(ProblemRequest.ProblemChoiceDTO problemChoiceDTO : problemInfoDTO.getProblemChoices()) {
            ProblemChoice problemChoice = ProblemChoice.create(problemChoiceDTO, problem);
            problem.getProblemChoices().add(problemChoice);
        }

        int idx = 0;
        for(ProblemRequest.ProblemAnswerDTO problemAnswerDTO : problemInfoDTO.getProblemAnswers()) {
            ProblemAnswer problemAnswer = ProblemAnswer.create(problemAnswerDTO, problem, idx);
            problem.getProblemAnswers().add(problemAnswer);
            idx++;
        }

        return problem;
    }
}

