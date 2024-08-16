package com.scf.battle.domain.dto.Problem;

import com.scf.battle.domain.enums.ItemType;
import com.scf.battle.domain.enums.Rarity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class Item {
    private String name;
    private ItemType type;
    private double[] probabilities;
    private Rarity rarity;
    public double getProbability(int difficulty) {
        return probabilities[difficulty - 1];
    }
}
