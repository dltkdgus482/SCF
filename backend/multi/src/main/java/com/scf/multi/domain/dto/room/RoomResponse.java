package com.scf.multi.domain.dto.room;

import lombok.Builder;
import lombok.Data;

public class RoomResponse {

    @Data
    @Builder
    public static class ListDTO {

        private String roomId;
        private String title;
        private Long hostId;
        private String hostname;
        private Integer maxPlayer;
        private Integer curPlayer;
        private Boolean isLock;
        private Boolean isStart;
        private Integer gameRound;
    }
}
