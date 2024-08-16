package com.scf.chat.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;
    private String roomId;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}