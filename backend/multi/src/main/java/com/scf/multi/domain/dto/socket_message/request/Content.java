package com.scf.multi.domain.dto.socket_message.request;

import com.scf.multi.domain.dto.problem.ProblemType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Content {

    private ProblemType problemType;
    private Integer submitTime;
    private Map<Integer, Integer> solve; // 빈칸 문제 대비 map 사용 ex) 0번 째 빈칸 : 1번 보기, 객관식 문제
    private String solveText; // 주관식 문제
}
