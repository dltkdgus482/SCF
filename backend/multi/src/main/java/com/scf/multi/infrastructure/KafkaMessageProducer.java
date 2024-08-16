package com.scf.multi.infrastructure;

import com.scf.multi.domain.dto.user.GameResult;
import com.scf.multi.domain.dto.user.Solved;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Solved> solvedKafkaTemplate;

    private final KafkaTemplate<String, GameResult> resultKafkaTemplate;

    public void sendSolved(Solved solved) {
        String topic = "solved";
        solvedKafkaTemplate.send(topic, solved);
        System.out.println("Sent SolvedDTO: " + solved);
    }

    public void sendResult(GameResult gameResult) {
        String topic = "game-result-multi";
        resultKafkaTemplate.send(topic, gameResult);
        System.out.println("Sent ResultDTO: " + gameResult);
    }
}

