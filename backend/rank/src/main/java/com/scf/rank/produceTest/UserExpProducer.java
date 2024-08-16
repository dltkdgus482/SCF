package com.scf.rank.produceTest;

import com.scf.rank.domain.model.UserExp;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExpProducer {

    private static final String TOPIC = "user-exp";

    private final KafkaTemplate<String, UserExp> kafkaTemplate;

    public void sendUserExpMessage(UserExp userExp) {
        kafkaTemplate.send(TOPIC, userExp.getUserId().toString(), userExp);
        System.out.println("Produced message: " + userExp);
    }
}
