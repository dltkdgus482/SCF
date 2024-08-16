package com.scf.user.profile.domain.dto.kafka;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solved {

    private Long problemId;
    private Long userId;
    private Map<Integer, Integer> solve;
    private String solveText;
    private Integer submitTime;
    private Boolean isCorrect;

    public static Solved fromMap(Map<String, Object> map) {
        Long problemId =
            map.containsKey("problemId") ? ((Number) map.get("problemId")).longValue() : null;
        Long userId = map.containsKey("userId") ? ((Number) map.get("userId")).longValue() : null;
        Map<Integer, Integer> solve =
            map.containsKey("solve") ? (Map<Integer, Integer>) map.get("solve") : null;
        String solveText = map.containsKey("solveText") ? (String) map.get("solveText") : null;
        Integer submitTime = map.containsKey("submitTime") ? (Integer) map.get("submitTime") : null;
        Boolean isCorrect = map.containsKey("isCorrect") ? (Boolean) map.get("isCorrect") : null;

        return Solved.builder()
            .problemId(problemId)
            .userId(userId)
            .solve(solve)
            .solveText(solveText)
            .submitTime(submitTime)
            .isCorrect(isCorrect)
            .build();
    }

}
