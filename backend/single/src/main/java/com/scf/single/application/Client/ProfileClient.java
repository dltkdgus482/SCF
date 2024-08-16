package com.scf.single.application.Client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ProfileClient {

    private final WebClient webClient;

    public ProfileClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://www.ssafy11s.com/profile").build();
    }

    // 경험치 누적시키는 로직 추가
    public void addExp(Long memberId) {
        try {
            webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/public/addExp")
                    .queryParam("memberId", memberId)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)  // 응답이 필요 없다면 Void.class 사용
                .block(); // 블록킹 방식으로 동기 처리

            System.out.println("경험치 누적이 완료되었습니다.");
        } catch (WebClientResponseException e) {
            // 예외 처리
            System.err.println("경험치 누적 요청에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 경험치를 조회해오는 로직 추가
    public Integer inquiryExp(Long memberId) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/public/getExp")
                    .queryParam("memberId", memberId)
                    .build())
                .retrieve()
                .bodyToMono(Integer.class)  // 경험치를 Integer 타입으로 받아옵니다.
                .block(); // 블록킹 방식으로 동기 처리

        } catch (WebClientResponseException e) {
            // 예외 처리
            System.err.println("경험치 조회 요청에 실패했습니다: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("알 수 없는 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }


}
