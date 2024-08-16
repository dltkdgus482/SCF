package com.scf.battle.application;

import com.scf.battle.domain.dto.Room.GameResultRoomDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.model.BattleGameRoom;
import com.scf.battle.infrastructure.KafkaMessageProducer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    public KafkaService(KafkaMessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    public void sendToKafkaSolved(BattleGameRoom room) {
        List<Player> players = Arrays.asList(room.getPlayerA(), room.getPlayerB());

        players.forEach(player ->
            Optional.ofNullable(player)
                .map(Player::getSolveds)
                .ifPresent(solveds -> solveds.forEach(kafkaMessageProducer::sendSolved))
        );
    }

    public void sendToKafkaGameResult(BattleGameRoom room, Integer determineValue) {
        GameResultRoomDTO gameResultRoomDTO = new GameResultRoomDTO(
            1,
            LocalDateTime.now(),
            room.getPlayerA().getUserId(),
            room.getPlayerB().getUserId(),
            determineValue
        );
        kafkaMessageProducer.sendGameResult(gameResultRoomDTO);
    }
}
