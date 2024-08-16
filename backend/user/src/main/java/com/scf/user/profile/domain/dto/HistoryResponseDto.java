package com.scf.user.profile.domain.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HistoryResponseDto {

    private String time;
    private int rank;
    private int score;
    private int gametype;
    private int partCnt;

    // LocalDateTime을 String으로 변환하는 메서드
    public static HistoryResponseDto from(LocalDateTime dateTime, int rank, int score,
        int gametype, int partCnt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 밀리초 제외
        String formattedTime = dateTime.format(formatter);
        return new HistoryResponseDto(formattedTime, rank, score, gametype, partCnt);
    }

}
