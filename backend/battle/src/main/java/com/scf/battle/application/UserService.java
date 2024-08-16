package com.scf.battle.application;

import com.scf.battle.domain.dto.User.UserCharaterTypeResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder,
                          @Value("${user.server.url}") String userServerUrl) {
        this.webClient = webClientBuilder.baseUrl(userServerUrl).build();
    }

    public UserCharaterTypeResponseDTO getUserCharaterType(Long memberId) {
        return webClient.get()
            .uri("http://www.ssafy11s.com/user/public/charaterType")  // 정확한 경로 설정
            .header("memberId", String.valueOf(memberId))  // 헤더에 memberId 추가
            .retrieve()
            .bodyToMono(UserCharaterTypeResponseDTO.class)
            .block();  // 응답을 UserCharaterTypeResponseDTO로 받아옴
    }

}
