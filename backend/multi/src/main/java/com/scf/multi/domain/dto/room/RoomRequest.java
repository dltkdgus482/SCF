package com.scf.multi.domain.dto.room;

import com.scf.multi.domain.model.MultiGameRoom;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

public class RoomRequest {

    @Data
    @Builder
    public static class CreateRoomDTO {

        @NotBlank(message = "제목을 입력해야 합니다.")
        @Size(max = 20, message = "제목은 최대 20자까지 가능합니다.")
        private String title;

        @Size(max = 20, message = "비밀번호는 최대 20자까지 가능합니다.")
        private String password;

        @NotNull(message = "최대 인원수를 입력해주세요.")
        @Min(value = 2, message = "최대 인원수는 최소 2명 이상이어야 합니다.")
        @Max(value = 100, message = "최대 인원수는 최대 100명 이하여야 합니다.")
        private Integer maxPlayer;

        @NotNull(message = "게임을 진행할 라운드를 입력해주세요.")
        @Min(value = 5, message = "최소 5라운드 이상을 선택해야 합니다.")
        @Max(value = 20, message = "최대 20라운드까지 가능합니다.")
        private Integer gameRound;

        public MultiGameRoom toEntity(String roomId, Long userId, String username) {

            return MultiGameRoom.builder()
                .roomId(roomId)
                .hostId(userId)
                .hostname(username)
                .title(title)
                .maxPlayer(maxPlayer)
                .password(password)
                .playRound(gameRound)
                .build();
        }
    }

}
