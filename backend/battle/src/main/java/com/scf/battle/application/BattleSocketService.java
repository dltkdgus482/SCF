package com.scf.battle.application;

import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.Problem.ProblemResponseDTO;
import com.scf.battle.domain.dto.Room.RoomResultResponseDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.model.BattleGameRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleSocketService {
    private final KafkaService kafkaService;
    private final RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<ProblemResponseDTO.SelectProblemDTO> getRoundSelectProblems(BattleGameRoom room) {
        return room.getProblemsForRound(room.getCurrentRound())
                .stream()
                .map(problem -> ProblemResponseDTO.SelectProblemDTO.builder()
                        .problemId(problem.getProblemId())
                        .title(problem.getTitle())
                        .problemType(problem.getProblemType())
                        .category(problem.getCategory())
                        .difficulty(problem.getDifficulty())
                        .problemContent(problem.getProblemContent())
                        .problemChoices(problem.getProblemChoices())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<ProblemResponseDTO.SelectProblemDTO> findSelectedProblem(
            List<ProblemResponseDTO.SelectProblemDTO> problems, Long problemId) {
        return problems.stream()
                .filter(problem -> problem.getProblemId().equals(problemId))
                .findFirst();
    }

    public void assignProblemToOpponent(BattleGameRoom room, Player selectingUser, ProblemResponseDTO.SelectProblemDTO problem) {
        if (selectingUser.getUserId().equals(room.getPlayerA().getUserId())) {
            room.setProblemForPlayerB(problem);
            return;
        }
        room.setProblemForPlayerA(problem);

    }

    public void handleRoundOver(BattleGameRoom room) {
        if (room.getCurrentRound().equals(room.getFinalRound()) || isAnyPlayerHpZero(room)) {
            determineWinner(room);
        } else {
            sendRoundProblemToRoom(room.getRoomId());
        }
    }

    private boolean isAnyPlayerHpZero(BattleGameRoom room) {
        return room.getPlayerA().getHp() <= 0 || room.getPlayerB().getHp() <= 0;
    }

    public void determineWinner(BattleGameRoom room) {
        GameResult gameResult = calculateGameResult(room);

        sendGameResultToKafka(room, gameResult);
        notifyClients(room, gameResult);
        room.initRoom();
        // BattleGameRoom 삭제
        // roomService.removeRoom(room.getRoomId());
    }

    private GameResult calculateGameResult(BattleGameRoom room) {
        int playerAHp = room.getPlayerA().getHp();
        int playerBHp = room.getPlayerB().getHp();

        Integer determineValue = 0;
        Long winnerId = null;
        Long loserId = null;

        if (playerAHp > playerBHp) {
            determineValue = 1;
            winnerId = room.getPlayerA().getUserId();
            loserId = room.getPlayerB().getUserId();
        } else if (playerBHp > playerAHp) {
            determineValue = 2;
            winnerId = room.getPlayerB().getUserId();
            loserId = room.getPlayerA().getUserId();
        }

        Map<String, Long> result = Map.of(
                "winner", winnerId != null ? winnerId : -1,
                "loser", loserId != null ? loserId : -1
        );

        return new GameResult(result, determineValue);
    }

    private void sendGameResultToKafka(BattleGameRoom room, GameResult gameResult) {
        kafkaService.sendToKafkaSolved(room); // kafka에게 유저별 문제 풀이 보내기
        kafkaService.sendToKafkaGameResult(room, gameResult.getDetermineValue()); // kafka에게 게임 결과 보내기
    }

    private void notifyClients(BattleGameRoom room, GameResult gameResult) {
        messagingTemplate.convertAndSend("/room/" + room.getRoomId(), new RoomResultResponseDTO(gameResult.getResult()));
    }


    public void leaveRoom(Long memberId, String roomId) {
        BattleGameRoom room = roomService.findById(roomId);
        if (memberId.equals(room.getHostId())) { // 방장인 경우
            //방 터짐, 다른 사용자한테도 나가게
            messagingTemplate.convertAndSend("/room/" + room.getRoomId(), "boom");
            roomService.removeRoom(roomId); // 방삭제
        } else {//아닌경우
            room.leavePlayerB();
        }
    }

    public void sendRoundProblemToRoom(String roomId) {
        BattleGameRoom room = roomService.findById(roomId);
        List<Problem> roundSelectProblems = room.getProblemsForRound(room.getCurrentRound());
        List<ProblemResponseDTO.ListDTO> roundSelectProblemsDTOs = roundSelectProblems.stream()
                .map(problem -> {
                    ProblemResponseDTO.ListDTO listDTO = ProblemResponseDTO.ListDTO.builder()
                            .problemId(problem.getProblemId())
                            .title(problem.getTitle())
                            .problemType(problem.getProblemType())
                            .category(problem.getCategory())
                            .difficulty(problem.getDifficulty())
                            .build();

                    listDTO.setItemBasedOnDifficulty(); // 아이템 선정 함수 호출

                    return listDTO;
                })
                .collect(Collectors.toList());

        messagingTemplate.convertAndSend("/room/" + roomId + "/RoundChoiceProblem", roundSelectProblemsDTOs);
    }

    private static class GameResult {
        private final Map<String, Long> result;
        private final Integer determineValue;

        public GameResult(Map<String, Long> result, Integer determineValue) {
            this.result = result;
            this.determineValue = determineValue;
        }

        public Map<String, Long> getResult() {
            return result;
        }

        public Integer getDetermineValue() {
            return determineValue;
        }
    }
}
