package com.scf.single.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentDetailResponseDto {

    private int pageNo;
    private String scriptContent;
    private int action;
    private int imageCount;

}
