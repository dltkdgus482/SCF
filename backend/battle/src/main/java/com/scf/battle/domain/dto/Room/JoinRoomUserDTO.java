package com.scf.battle.domain.dto.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomUserDTO {
    private Long userId;
    private String username;
}