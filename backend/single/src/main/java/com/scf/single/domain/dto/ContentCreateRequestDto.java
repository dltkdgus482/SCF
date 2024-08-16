package com.scf.single.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentCreateRequestDto {

    private String contentType;
    private String title;
    private int contentExp;


}
