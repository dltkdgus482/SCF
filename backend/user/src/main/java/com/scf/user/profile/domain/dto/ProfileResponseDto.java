package com.scf.user.profile.domain.dto;

import java.time.LocalDate;

import com.scf.user.member.domain.dto.UserCharacterResponseDTO;
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
public class ProfileResponseDto {

    private String name;
    private LocalDate birth;
    private Integer exp;
    private UserCharacterResponseDTO UserCharacter;
    private String school;
}
