package com.scf.user.member.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserInfoResponseDto {

    private String userId;
    private String name;
    private String schoolName;
    private LocalDate birth;

}
