package com.scf.user.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Getter
@AllArgsConstructor
public class UserCharacterResponseDTO {
    private int characterType;
    private String characterRarity;
    private String clothRarity;
}
