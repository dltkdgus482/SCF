package com.scf.user.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfotoSingleResponseDto {

    private Long memberId;
    private String userName;

}
