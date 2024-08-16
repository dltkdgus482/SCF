package com.scf.user.member.domain.dto.charater;

import com.scf.user.member.domain.enums.Rarity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class ClothingType {

    private final int type;
    private final double probability;
    private final Rarity rarity;
}
