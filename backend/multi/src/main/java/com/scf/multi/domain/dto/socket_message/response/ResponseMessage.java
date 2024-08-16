package com.scf.multi.domain.dto.socket_message.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {

    private String type;
    private Object payload;
}
