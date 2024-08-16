package com.scf.user.profile.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HistoryListResponseDto {

    public List<HistoryResponseDto> historyLists;

}
