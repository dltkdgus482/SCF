package com.scf.multi.domain.event;

public class GameStartedEvent {

    private final String roomId;

    public GameStartedEvent(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
