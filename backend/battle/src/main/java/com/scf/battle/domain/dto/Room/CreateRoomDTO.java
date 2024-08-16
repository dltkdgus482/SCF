package com.scf.battle.domain.dto.Room;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateRoomDTO {

    @NotBlank(message = "제목을 입력해야 합니다.")
    @Size(max = 20, message = "제목은 최대 20자까지 가능합니다.")
    private String title;

    @Size(max = 120, message = "비밀번호은 최대 20자까지 가능합니다.")
    private String password;

    @NotNull(message = "라운드를 입력해야합니다.")
    @Min(value = 2, message = "라운드는 최소 1라운드 이상 선택해야 합니다.")
    @Max(value = 10, message = "라운드는 최대 10라운드까지 가능합니다.")
    private Integer round;
}
