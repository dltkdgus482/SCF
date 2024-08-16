package com.scf.user.profile.application.client;

import com.scf.user.profile.domain.dto.ProblemResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProblemClient {

    private final WebClient webClient;

    public ProblemClient(WebClient.Builder webClientBuilder) {
        // 임시 url 지정
        this.webClient = webClientBuilder.baseUrl("http://www.ssafy11s.com/problem").build();
    }

    // 문제 상세 조회
    public Mono<ProblemResponseDto> getProblemById(Long problemId) {
        return webClient.get()
            .uri("/{problemId}", problemId)
            .retrieve()
            .bodyToMono(ProblemResponseDto.class);
    }


}
