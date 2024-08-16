package com.scf.user.member.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 서버가 사용자의 요청을 처리할 때 사용하는 DTO
public class PasswordResetRequestDto {

    private String userId;
    private String newPassword;
}
