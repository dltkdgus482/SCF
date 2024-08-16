package com.scf.multi.domain.dto.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GameResult {

    private Integer gameType; // 0 멀티, 1 배틀
    private LocalDateTime localDateTime;
    private List<Rank> gameRank = new ArrayList<>();

    @Builder
    public GameResult(List<Rank> gameRank) {
        this.gameType = 0;
        this.localDateTime = LocalDateTime.now();
        this.gameRank = gameRank;
    }
}
