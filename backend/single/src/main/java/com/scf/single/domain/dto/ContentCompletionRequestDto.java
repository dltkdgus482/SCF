package com.scf.single.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentCompletionRequestDto {

    private Long memberId;
    private int contentId;

}
