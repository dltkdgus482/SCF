package com.scf.battle.domain.dto.Problem;

import java.util.List;

import com.scf.battle.domain.enums.ProblemType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Problem {

    private Long problemId;
    private String title;
    private ProblemType problemType;
    private String category;
    private int difficulty;
    private ProblemContent problemContent;
    private List<ProblemChoice> problemChoices;
    private List<ProblemAnswer> problemAnswers;

    @Builder
    public Problem(Long problemId, String title, ProblemType problemType, String category,
        int difficulty, ProblemContent problemContent, List<ProblemChoice> problemChoices,
        List<ProblemAnswer> problemAnswers) {
        this.problemId = problemId;
        this.title = title;
        this.problemType = problemType;
        this.category = category;
        this.difficulty = difficulty;
        this.problemContent = problemContent;
        this.problemChoices = problemChoices;
        this.problemAnswers = problemAnswers;
    }
}
