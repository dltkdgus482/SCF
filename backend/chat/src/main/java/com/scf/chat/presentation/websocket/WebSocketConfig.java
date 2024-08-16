package com.scf.chat.presentation.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커가 /room으로 시작하는 주제를 구독하는 클라이언트에게 메시지 전송
        config.enableSimpleBroker("/room");
        // 클라이언트에서 서버로 보낼 메시지의 prefix
        config.setApplicationDestinationPrefixes("/send");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 등록 및 SockJS 지원
        registry.addEndpoint("/ws-chat")
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }
}
