package com.scf.battle.domain.dto.Problem;

import java.util.List;
import java.util.Random;

import com.scf.battle.domain.enums.ProblemType;
import lombok.Builder;
import lombok.Data;

public class ProblemResponseDTO {

    @Data
    @Builder
    public static class ListDTO {

        private Long problemId;
        private String title;
        private ProblemType problemType;
        private String category;
        private Integer difficulty;
        private Item item;
        public void setItemBasedOnDifficulty() {
            Random random = new Random();
            double randomValue = random.nextDouble();

            double cumulativeProbability = 0.0;
            for (Item item : ItemList.items) {
                cumulativeProbability += item.getProbability(this.difficulty);
                if (randomValue <= cumulativeProbability) {
                    this.item = item;
                    break;
                }
            }
        }
    }

    @Data
    @Builder
    public static class SelectProblemDTO {

        private Long problemId;
        private String title;
        private ProblemType problemType;
        private String category;
        private Integer difficulty;
        private ProblemContent problemContent;
        private List<ProblemChoice> problemChoices;

    }
}
