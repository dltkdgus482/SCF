package com.scf.battle.domain.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Player {

    private Long userId;
    private String username;
    private Integer Hp;
    private final List<Solved> solveds = new ArrayList<>();

    public void addSolved(Solved solved) {
        this.solveds.add(solved);
    }

}
