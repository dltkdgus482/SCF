package com.scf.user.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Getter
@AllArgsConstructor
public class UserCharaterTypeResponseDTO {
    private int characterType;
    private String characterRarity;
}
