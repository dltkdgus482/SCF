package com.scf.user.profile.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class SolvedProblemsListDto {

    private List<SolvedProblemResponseDto> solvedList;

}
