package com.scf.user.member.application.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ContentClient {

    private final WebClient webClient;

    public ContentClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://www.ssafy11s.com/single").build();
    }

    public Mono<Void> initializeCompletionStatus(Long memberId) {
        return webClient.post()
            .uri(uriBuilder -> uriBuilder.path("/public/{memberId}").build(memberId))
            .retrieve()
            .bodyToMono(Void.class);
    }
}
