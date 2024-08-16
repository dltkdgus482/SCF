package com.scf.battle.presentation.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-battle") //web socket connection이 최초로 이루어지는 곳(handshake)
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // @Controller 객체의 @MessageMapping 메서드로 라우팅, 클라이언트가 서버로 메시지 보낼 URL 접두사(pub)
        registry.setApplicationDestinationPrefixes("/send");
        // /topic 로 클라이언트로 메시지 전달(sub)
        registry.enableSimpleBroker("/room");
    }
}
