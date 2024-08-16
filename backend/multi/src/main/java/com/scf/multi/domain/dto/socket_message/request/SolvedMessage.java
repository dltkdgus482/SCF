package com.scf.multi.domain.dto.socket_message.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolvedMessage {

    private String type;
    private Content content;
}
