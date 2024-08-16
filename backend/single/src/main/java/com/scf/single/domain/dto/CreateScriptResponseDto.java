package com.scf.single.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateScriptResponseDto {

    // 개별의 스크립트
    private int pageNo;
    private String scriptContent;
    private int action;
    private int imageCount;

}
