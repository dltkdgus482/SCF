package com.scf.rank.domain.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserExp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder
    public UserExp(Long userId, String name, Integer exp) {
        this.userId = userId;
        this.name = name;
        this.exp = exp;
    }

    private Long userId;
    private String name;
    private int exp;

    public void addExp(int exp) {
        this.exp += exp;
    }
}
