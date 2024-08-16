package com.scf.chat.presentation.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.scf.chat.domain.ChatMessage;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{roomId}") // 여기로 전송되면 메서드 호출 -> WebSocketConfig prefixes 에서 적용한건 앞에 생략
    public void sendMessage(@DestinationVariable String roomId, ChatMessage chatMessage) {
        try {
            simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        } catch (Exception e) {
            System.out.println("Error sending message to room " + roomId + ": " + e.getMessage());
            // 클라이언트에게 에러 메시지 전송 (옵션)
            chatMessage.setContent("Error sending message: " + e.getMessage());
            simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        }
    }

    @MessageMapping("/chat/{roomId}/enter")
    public void enterRoom(@DestinationVariable String roomId, ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // 사용자 이름을 웹소켓 세션에 저장
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            chatMessage.setContent(chatMessage.getSender() + " has entered the room.");
            simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        } catch (Exception e) {
            System.out.println("Error entering room " + roomId + ": " + e.getMessage());
            // 클라이언트에게 에러 메시지 전송 (옵션)
            chatMessage.setContent("Error entering room: " + e.getMessage());
            simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        }
    }

    @MessageMapping("/chat/{roomId}/leave")
    public void leaveRoom(@DestinationVariable String roomId, ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if (username != null) {
                chatMessage.setSender(username);
                chatMessage.setContent(username + " has left the room.");
                simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
            } else {
                System.out.println("Username not found in session attributes for room " + roomId);
                chatMessage.setContent("Error: Username not found in session.");
                simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
            }
        } catch (Exception e) {
            System.out.println("Error leaving room " + roomId + ": " + e.getMessage());
            // 클라이언트에게 에러 메시지 전송 (옵션)
            chatMessage.setContent("Error leaving room: " + e.getMessage());
            simpMessagingTemplate.convertAndSend("/room/" + roomId, chatMessage);
        }
    }
}
