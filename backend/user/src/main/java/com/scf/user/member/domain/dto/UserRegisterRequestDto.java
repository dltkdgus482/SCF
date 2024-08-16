package com.scf.user.member.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRegisterRequestDto {

    private String userId;
    private String password;
    private String name;
    private String email;
    private String schoolName;
    private LocalDate birth;
    private int characterType;

}
