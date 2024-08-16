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
public class SolvedProblemKafkaRequestDto {

    private Long problemId; // 문제 아이디
    private boolean isCorrect; // 정답여부
    private String choice; // 주관식
    private Map<Integer, Integer> choiceText; // 빈칸, 객관식
}
