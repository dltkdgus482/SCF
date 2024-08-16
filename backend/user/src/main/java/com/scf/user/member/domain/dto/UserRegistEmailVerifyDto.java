package com.scf.user.member.domain.dto;

import lombok.Data;

@Data
public class UserRegistEmailVerifyDto {

    private String email;
    private String code;
}
