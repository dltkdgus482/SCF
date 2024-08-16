package com.scf.user.member.application.service;

import com.scf.user.member.domain.dto.charater.CharacterType;
import com.scf.user.member.domain.dto.charater.ClothingType;
import com.scf.user.member.domain.enums.Rarity;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GachaService {
    @Getter
    public static final List<ClothingType> clothingTypes = new ArrayList<>();
    @Getter
    public static final List<CharacterType> characterTypes = new ArrayList<>();

    static {
        clothingTypes.add(new ClothingType(1, 0.025, Rarity.EPIC));
        clothingTypes.add(new ClothingType(2, 0.005, Rarity.COMMON));
        clothingTypes.add(new ClothingType(3, 0.392, Rarity.LEGENDARY)); // 9.8%
        clothingTypes.add(new ClothingType(7, 0.025, Rarity.EPIC));
        clothingTypes.add(new ClothingType(10, 0.098, Rarity.COMMON)); // 9.8%
        clothingTypes.add(new ClothingType(11, 0.025, Rarity.EPIC));
        clothingTypes.add(new ClothingType(12, 0.025, Rarity.EPIC));
        clothingTypes.add(new ClothingType(13, 0.005, Rarity.COMMON));
        clothingTypes.add(new ClothingType(14, 0.298, Rarity.LEGENDARY)); // 10.2%
        clothingTypes.add(new ClothingType(15, 0.100, Rarity.COMMON)); // 10.2%
    }

    static {
        characterTypes.add(new CharacterType(1, 0.148333, Rarity.COMMON)); // 7.8%로 설정된 항목
        characterTypes.add(new CharacterType(2, 0.148333, Rarity.COMMON)); // 7.4%로 설정된 항목
        characterTypes.add(new CharacterType(3, 0.148333, Rarity.COMMON)); // 7.4%로 설정된 항목
        characterTypes.add(new CharacterType(4, 0.148333, Rarity.COMMON)); // 7.4%로 설정된 항목
        characterTypes.add(new CharacterType(5, 0.148333, Rarity.COMMON)); // 7.4%로 설정된 항목
        characterTypes.add(new CharacterType(6, 0.148333, Rarity.COMMON)); // 7.4%로 설정된 항목
        characterTypes.add(new CharacterType(7, 0.025, Rarity.EPIC)); // 2.5%로 설정된 항목
        characterTypes.add(new CharacterType(8, 0.025, Rarity.EPIC)); // 2.5%로 설정된 항목
        characterTypes.add(new CharacterType(9, 0.025, Rarity.EPIC)); // 2.5%로 설정된 항목
        characterTypes.add(new CharacterType(10, 0.025, Rarity.EPIC)); // 2.5%로 설정된 항목
        characterTypes.add(new CharacterType(11, 0.005, Rarity.LEGENDARY)); // 0.5%로 설정된 항목
        characterTypes.add(new CharacterType(12, 0.005, Rarity.LEGENDARY)); // 0.5%로 설정된 항목
    }

    public ClothingType drawClothingType() {
        Random random = new Random();
        double randomValue = random.nextDouble(); // 0.0 <= randomValue < 1.0
        double cumulativeProbability = 0.0;

        for (ClothingType clothingType : clothingTypes) {
            cumulativeProbability += clothingType.getProbability();
            if (randomValue <= cumulativeProbability) {
                return clothingType;  // ClothingType 객체 자체를 반환
            }
        }
        return clothingTypes.get(0); // 기본값으로 첫 번째 항목을 반환
    }

    public CharacterType drawCharacterType() {
        Random random = new Random();
        double randomValue = random.nextDouble(); // 0.0 <= randomValue < 1.0
        double cumulativeProbability = 0.0;

        for (CharacterType characterType : characterTypes) {
            cumulativeProbability += characterType.getProbability();
            if (randomValue <= cumulativeProbability) {
                return characterType;  // CharacterType 객체 자체를 반환
            }
        }
        return characterTypes.get(0); // 기본값으로 첫 번째 항목을 반환
    }

}
