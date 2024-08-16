package com.scf.user.profile.application.service;

import com.scf.user.profile.domain.dto.kafka.RenewExp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, RenewExp> renewExpKafkaTemplate;

    // 전체 누적된 경험치를 전송
    public void sendProcessedTotalGameResults(RenewExp renewExp) {
        String topic = "user-total-exp";
        renewExpKafkaTemplate.send(topic, renewExp);
        System.out.println("Sent processed Exp: " + renewExp);
    }

    // 게임 끝날때 마다 얻은 경험치를 전송(싱글, 배틀, 멀티)
    public void sendProcessedGameResults(RenewExp renewExp) {
        String topic = "user-exp";
        renewExpKafkaTemplate.send(topic, renewExp);
        System.out.println("Sent processed Exp: " + renewExp);
    }

}
