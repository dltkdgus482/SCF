package com.scf.user.member.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 사용자가 비밀번호 재설정을 요청할 때 필요한 정보
public class UserPasswordRequestDto {

    private String userId;

}
