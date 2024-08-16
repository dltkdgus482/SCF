package com.scf.single.application.service;

import com.scf.single.domain.dto.kafka.RenewExp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, RenewExp> renewExpKafkaTemplate;

    // 싱글 컨텐츠 끝날때마다 얻은 경험치를 전송합니다.
    public void sendProcessedSingleContents(RenewExp renewExp) {
        String topic = "user-exp";
        renewExpKafkaTemplate.send(topic, renewExp);
        System.out.println("Sent processed Exp: " + renewExp);
    }

    // 전체 누적된 경험치를 전송
    public void sendProcessedTotalExp(RenewExp renewExp) {
        String topic = "user-total-exp";
        renewExpKafkaTemplate.send(topic, renewExp);
        System.out.println("Sent processed Exp: " + renewExp);
    }

}
