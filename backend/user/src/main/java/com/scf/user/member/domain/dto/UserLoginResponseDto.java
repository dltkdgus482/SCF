package com.scf.user.member.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String name;
    private Long memberId;


    public UserLoginResponseDto(String accessToken, String refreshToken, Long memberId,
        String name) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.name = name;
    }
}
