package com.scf.user.profile.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// 게임 결과 등록하는 DTO
public class SubmitGameResultDto {

    private Integer gameType;
    private Integer partCnt;
    private Integer ranking;
    private Integer score;
    private LocalDateTime time;
    private Integer memberId;

}
