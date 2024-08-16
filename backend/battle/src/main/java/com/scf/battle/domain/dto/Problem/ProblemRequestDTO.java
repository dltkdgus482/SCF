package com.scf.battle.domain.dto.Problem;

import com.scf.battle.domain.enums.ProblemType;
import java.util.Map;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRequestDTO {

    @NotBlank(message = "문제 유형은 필수 입력 항목입니다.")
    private ProblemType problemType;

    @NotBlank(message = "문제 ID는 필수 입력 항목입니다.")
    private Long problemId;

    @NotBlank(message = "유저 ID는 필수 입력 항목입니다.")
    private Long userId;

    private Map<Integer, Integer> solve;

    private String solveText;

    @NotBlank(message = "제출 시간은 필수 입력 항목입니다.")
    @Max(value = 30, message = "제출 시간은 최대 30초입니다.")
    private Integer submitTime;

    @NotBlank(message = "현재 라운드는 필수 입력 항목입니다.")
    @Min(value = 1, message = "라운드는 최소 0라운드 이상 선택해야 합니다.")
    @Max(value = 10, message = "라운드는 최대 9라운드까지 가능합니다.")
    private Integer round;
}
