package com.scf.user.profile.application.service;

import com.scf.user.profile.domain.dto.kafka.BattleGameResult;
import com.scf.user.profile.domain.dto.kafka.MultiGameResult;
import com.scf.user.profile.domain.dto.kafka.Rank;
import com.scf.user.profile.domain.dto.kafka.RenewExp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameResultConsumer {

    private final KafkaMessageProducer kafkaMessageProducer;
    private final ProfileService profileService;

    // battle 모드
    @KafkaListener(topics = "game-result-battle", groupId = "game-results-group", containerFactory = "battleGameResultKafkaListenerContainerFactory")
    public void processGameResults(BattleGameResult battleGameResult) {
        // 각 유저의 경험치를 업데이트
        Long playerA = battleGameResult.getPlayerAId();
        Long playerB = battleGameResult.getPlayerBId();
        int win = battleGameResult.getResult(); // 누가 이겼는가
        int gameType = battleGameResult.getGameType(); // 기본 경험치 게임타입 반영 (임시)

        // playerA의 기존 경험치
        int expA = profileService.getProfileInfo(playerA).getExp();
        int expB = profileService.getProfileInfo(playerB).getExp();

        // username
        String playerAName = profileService.getProfileInfo(playerA).getName();
        String playerBName = profileService.getProfileInfo(playerB).getName();

        // 해당 게임에서 얻은 경험치
        List<Integer> players = profileService.calculateBattleExp(win);
        // 해당 게임에서 얻은 경험치를 전송합니다.
        RenewExp a = new RenewExp(playerA, playerAName, players.get(0));
        RenewExp b = new RenewExp(playerB, playerBName, players.get(1));

        kafkaMessageProducer.sendProcessedGameResults(a);
        kafkaMessageProducer.sendProcessedGameResults(b);

        // 게임결과와 해당 게임에서 얻은 경험치를 db에 저장합니다.
        profileService.submitBattleGameResultList(battleGameResult);

        // 경험치를 업데이트.
        int newExpA = expA + players.get(0);
        int newExpB = expB + players.get(1);


        profileService.updateExpoint(playerA, players.get(0));
        profileService.updateExpoint(playerB, players.get(1));

        // 처리된 데이터를 다시 전송
        // List형식이 아닌 RenewExp 객체로 보내도록 수정합니다.
        RenewExp arenew = new RenewExp(playerA, playerAName, newExpA);
        RenewExp brenew = new RenewExp(playerB, playerBName, newExpB);

        // 게임 참여한 유저 수 만큼 새로운 게임 결과가 반영된 개인의 경험치를 전송합니다.
        kafkaMessageProducer.sendProcessedTotalGameResults(arenew);
        kafkaMessageProducer.sendProcessedTotalGameResults(brenew);

    }

    // multi 모드
    @KafkaListener(topics = "game-result-multi", groupId = "game-results-group", containerFactory = "multiGameResultkafkaListenerContainerFactory")
    public void processMultiGameResults(MultiGameResult multiGameResult) {
        System.out.println("Processing multi game results: " + multiGameResult.getGameRank());

        // multi 게임이면
        if (multiGameResult.getGameType().equals(0)) {
            // 해당 게임에서 얻은 경험치를 user-exp 토픽으로 전송합니다.
            // 해당 게임에서 얻은 경험치
            List<Rank> ranks = multiGameResult.getGameRank();
            for (Rank rank : ranks) {
                Integer gameExp = profileService.calculateMultiExp(ranks.size(), rank.getScore(),
                    rank.getRank());
                RenewExp renewExp = new RenewExp(rank.getUserId(), rank.getUsername(), gameExp);
                // user-exp 토픽으로 전송
                kafkaMessageProducer.sendProcessedGameResults(renewExp);
            }

            // 경험치를 업데이트
            List<RenewExp> updatedExp = updateExperiencePoints(multiGameResult.getGameRank());

            // 게임결과와 받아온 경험치를 db에 저장합니다.
            profileService.submitMultiGameResultList(multiGameResult);

            // 처리된 데이터를 다시 전송
            // 게임 참여한 유저 수 만큼 for문을 돌면서 새로운 게임 결과가 반영된 개인의 경험치를 전송합니다.
            for (RenewExp exp : updatedExp) {
                kafkaMessageProducer.sendProcessedTotalGameResults(exp);
            }
        }
    }

    // exp를 업데이트.
    private List<RenewExp> updateExperiencePoints(List<Rank> ranks) {
        // ranks 리스트를 순회하면서 각 사용자 경험치를 업데이트.
        List<RenewExp> updatedExpList = new ArrayList<>();

        for (Rank rank : ranks) {
            Long memberId = rank.getUserId();
            String name = rank.getUsername();

            // 기존 경험치
            Integer currentExp = profileService.getProfileInfo(memberId).getExp();

            // 반영할 경험치
            Integer newExp =
                currentExp + profileService.calculateMultiExp(ranks.size(), rank.getScore(),
                    rank.getRank());

            // 경험치를 업데이트
            profileService.updateExp(memberId, newExp);

            // 갱신된 경험치 정보를 DTO에 담기
            RenewExp renewExp = new RenewExp(memberId, name, newExp);
            updatedExpList.add(renewExp);
        }

        return updatedExpList;
    }
}

