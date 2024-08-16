package com.scf.rank.consumer;

import com.scf.rank.application.RankService;
import com.scf.rank.constant.RedisRankType;
import com.scf.rank.domain.model.UserExp;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class KafkaUserExpConsumer {

    private final RankService rankService;
    @KafkaListener(topics = "user-total-exp", groupId = "ranking-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserTotalExp(UserExp userExp, Acknowledgment acknowledgment) {

        rankService.updateTotalRank(userExp); // 전체 기간 업데이트

        acknowledgment.acknowledge(); // 메시지 처리 완료 후 ACK 전송
    }

    @KafkaListener(topics = "user-exp", groupId = "ranking-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeUserExp(UserExp userExp, Acknowledgment acknowledgment) {

        LocalDate today = LocalDate.now();
        String dailyPrefix = rankService.getDailyPrefix(today);
        String weeklyPrefix = rankService.getWeeklyPrefix(today);
        rankService.updateRank(userExp, dailyPrefix);
        rankService.updateRank(userExp, weeklyPrefix);

        acknowledgment.acknowledge(); // 메시지 처리 완료 후 ACK 전송
    }
}
