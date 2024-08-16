package com.scf.battle.domain.dto.Room;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GameResultRoomDTO {

    private Integer gameType;
    private LocalDateTime gameDateTime;
    private Long playerAId;
    private Long playerBId;
    private Integer result;

}