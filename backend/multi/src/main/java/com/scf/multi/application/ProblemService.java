package com.scf.multi.application;

import com.scf.multi.domain.dto.problem.Problem;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProblemService {

    private final WebClient webClient;

    public ProblemService(WebClient.Builder webClientBuilder,
        @Value("${problem.server.url}") String problemServerUrl) {
        this.webClient = webClientBuilder.baseUrl(problemServerUrl).build();
    }

    public List<Problem> getProblems(Integer playRound) {

        return webClient.get()
            .uri("?limit=" + playRound)
            .retrieve()
            .bodyToFlux(Problem.class)
            .collectList()
            .block();
    }
}
