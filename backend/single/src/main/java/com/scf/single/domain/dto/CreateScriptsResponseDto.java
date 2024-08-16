package com.scf.single.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateScriptsResponseDto {

    List<CreateScriptResponseDto> scriptResponseList;

}
