package com.scf.battle.presentation.websocket;

import com.scf.battle.application.RoomService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final RoomService roomService;

    public WebSocketEventListener(RoomService roomService) {
        this.roomService = roomService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (roomId != null && userId != null) {
            roomService.handleUserDisconnect(roomId, userId);
        }
    }
}
