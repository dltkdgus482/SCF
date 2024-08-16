package com.scf.battle.domain.dto.User;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FightDTO {

    private Long userId;
    private Boolean isAttack;
    private Integer power;
}
