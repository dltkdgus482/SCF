package com.scf.battle.infrastructure;

import java.util.List;

import com.scf.battle.domain.dto.Room.GameResultRoomDTO;
import com.scf.battle.domain.dto.User.Solved;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Solved> solvedKafkaTemplate;
    private final KafkaTemplate<String, GameResultRoomDTO> gameResultKafkaTemplate;

    public void sendSolved(Solved solved) {
        String topic = "solved";
        solvedKafkaTemplate.send(topic, solved);
        System.out.println("Sent SolvedDTO: " + solved);
    }

    public void sendGameResult(GameResultRoomDTO gameResultRoomDTO) {
        String topic = "game-result-battle";
        gameResultKafkaTemplate.send(topic, gameResultRoomDTO);
        System.out.println("Sent GameResult: " + gameResultRoomDTO);
    }

}