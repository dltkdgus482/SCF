package com.scf.battle.domain.dto.Room;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomRequestDTO {
    @Size(max = 120, message = "비밀번호은 최대 20자까지 가능합니다.")
    private String password;

}
