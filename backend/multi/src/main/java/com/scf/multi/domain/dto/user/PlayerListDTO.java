package com.scf.multi.domain.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayerListDTO {

    private Long userId;
    private String username;

    @Builder
    public PlayerListDTO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static PlayerListDTO fromPlayer(Player player) {
        return PlayerListDTO.builder()
            .userId(player.getUserId())
            .username(player.getUsername())
            .build();
    }
}
