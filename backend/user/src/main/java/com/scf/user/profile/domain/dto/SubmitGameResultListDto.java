package com.scf.user.profile.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// 게임 결과들을 담을 DTO
public class SubmitGameResultListDto {

    List<SubmitGameResultDto> submitGameResultList;

}
