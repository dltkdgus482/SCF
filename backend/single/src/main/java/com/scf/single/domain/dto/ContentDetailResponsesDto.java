package com.scf.single.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentDetailResponsesDto {

    List<ContentDetailResponseDto> detailList;

}
