package com.scf.user.member.domain.dto;

import com.scf.user.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDto {

    private String userId;
    private String name;

    public UserRegisterResponseDto(Member member) {
        this.userId = member.getUserId();
        this.name = member.getName();
    }
}
