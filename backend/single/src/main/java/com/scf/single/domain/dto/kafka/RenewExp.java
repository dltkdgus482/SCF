package com.scf.single.domain.dto.kafka;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenewExp {

    private Long userId;
    private String name;
    private int exp;

    @Builder
    public RenewExp(Long userId, String name, int exp) {
        this.userId = userId;
        this.name = name;
        this.exp = exp;
    }

}
