package com.scf.multi.presentation.websocket;

import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        URI uri = request.getURI();
        String query = uri.getQuery();

        Map<String, String> queryParams = parseQueryParams(query);

        String roomId = queryParams.get("roomId");
        String userIdStr = queryParams.get("userId");
        String username = queryParams.get("username");

        if (roomId != null) {
            attributes.put("roomId", roomId);
        }
        if (userIdStr != null) {
            Long userId = Long.valueOf(userIdStr);
            attributes.put("userId", userId);
        }
        if (username != null) {
            attributes.put("username", username);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    queryParams.put(key, value);
                }
            }
        }
        return queryParams;
    }
}
