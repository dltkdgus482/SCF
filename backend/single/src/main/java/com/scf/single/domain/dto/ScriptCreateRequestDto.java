package com.scf.single.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScriptCreateRequestDto {

    // Integer -> null 포함 가능.
    private Integer pageNo;
    private String scriptContent;
    private Integer action;
    private Integer imageCount;

}
