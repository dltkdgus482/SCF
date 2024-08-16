package com.scf.user.profile.domain.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rank {

    // kafka에 쓰기 위함.
    private Long userId;
    private String username;
    private int score;
    private int rank;
}
