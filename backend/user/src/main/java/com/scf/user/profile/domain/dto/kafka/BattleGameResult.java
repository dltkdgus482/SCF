package com.scf.user.profile.domain.dto.kafka;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BattleGameResult {

    private Integer gameType;
    private LocalDateTime gameDateTime;
    private Long playerAId;
    private Long playerBId;
    private Integer result; // 누가 이겼는지

}
