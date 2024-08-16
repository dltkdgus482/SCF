package com.scf.single.application.Client;

import com.scf.single.domain.dto.UserInfoListResponseDto;
import com.scf.single.domain.dto.UserNameDto;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://www.ssafy11s.com/user").build();
    }

    // 유저 리스트 검색
    public Mono<UserInfoListResponseDto> getUserList() {
        return webClient.get()
            .uri("/public/list")
            .retrieve()
            .bodyToMono(UserInfoListResponseDto.class)
            .doOnError(e -> System.err.println("Error: " + e.getMessage())); // 오류 로그 출력
    }

    // 유저 이름 검색
    public Mono<UserNameDto> getUsername(Long memberId) {
        return webClient.get()
            .uri("/public/name/{memberId}", memberId)
            .retrieve()  // 응답을 받아오기 위한 메서드
            .bodyToMono(UserNameDto.class);  // 응답 본문을 UserNameDto로 변환
    }

}
