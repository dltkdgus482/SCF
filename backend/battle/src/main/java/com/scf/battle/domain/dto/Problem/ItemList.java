package com.scf.battle.domain.dto.Problem;

import com.scf.battle.domain.enums.ItemType;
import com.scf.battle.domain.enums.Rarity;

import java.util.Arrays;
import java.util.List;

public class ItemList {
    public static final List<Item> items = Arrays.asList(
            new Item("체력 회복 아이템", ItemType.BUFF, new double[] {0.35, 0.3, 0.2, 0.1, 0.05}, Rarity.LEGENDARY),
            new Item("시간 연장", ItemType.BUFF, new double[] {0.35, 0.3, 0.2, 0.1, 0.05}, Rarity.LEGENDARY),
            new Item("혼란의 스크린", ItemType.DEBUFF, new double[] {0.05, 0.1, 0.2, 0.3, 0.35}, Rarity.COMMON),
            new Item("시간 단축", ItemType.DEBUFF, new double[] {0.05, 0.1, 0.2, 0.3, 0.35}, Rarity.COMMON),
            new Item("체력 감소 아이템", ItemType.DEBUFF, new double[] {0.05, 0.1, 0.2, 0.3, 0.35}, Rarity.COMMON)
    );
}
