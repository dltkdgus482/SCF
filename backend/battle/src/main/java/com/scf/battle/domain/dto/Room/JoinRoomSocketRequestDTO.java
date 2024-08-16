package com.scf.battle.domain.dto.Room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomSocketRequestDTO {

    @NotBlank(message = "방ID가 필요합니다.")
    private String roomId;

    @NotBlank(message = "유저 ID가 필요합니다.")
    private Long userId;

    @NotBlank(message = "유저 이름이 필요합니다.")
    private String username;

    @Size(max = 120, message = "비밀번호은 최대 20자까지 가능합니다.")
    private String roomPassword;
}
