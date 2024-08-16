package com.scf.user.profile.domain.dto.kafka;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MultiGameResult {

    private Integer gameType; // 0 멀티, 1 배틀
    private LocalDateTime localDateTime;
    private List<Rank> gameRank = new ArrayList<>();

    @Builder
    public MultiGameResult(List<Rank> gameRank) {
        this.gameType = 0;
        this.localDateTime = LocalDateTime.now();
        this.gameRank = gameRank;
    }

}
