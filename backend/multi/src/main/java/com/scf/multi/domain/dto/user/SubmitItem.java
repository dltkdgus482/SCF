package com.scf.multi.domain.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SubmitItem {

    private Long userId;
    private Boolean isSubmit;

    @Builder
    public SubmitItem(Long userId, Boolean isSubmit) {
        this.userId = userId;
        this.isSubmit = isSubmit;
    }
}
