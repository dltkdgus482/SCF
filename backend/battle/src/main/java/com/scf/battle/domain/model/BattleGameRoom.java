package com.scf.battle.domain.model;

import com.scf.battle.domain.dto.Problem.ProblemResponseDTO;
import com.scf.battle.domain.dto.User.FightDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.User.UserCharaterTypeResponseDTO;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class BattleGameRoom {

    private String roomId;
    private Long hostId;
    private String title;
    private String password;
    private String hostUsername;

    private final List<List<Problem>> roundProblems = new ArrayList<>(); // 라운드 마다 3문제로 나누기 위함
    @Setter
    private Player playerA;
    @Setter
    private Player playerB;
    private Boolean isAttack;
    private Boolean isStart;
    private Integer finalRound;
    private Integer currentRound;
    private Boolean hasPlayerASubmitted;
    private Boolean hasPlayerBSubmitted;
    private UserCharaterTypeResponseDTO hostCharacter;

    // 각 유저가 풀어야 할 문제를 저장할 필드
    @Setter
    private ProblemResponseDTO.SelectProblemDTO problemForPlayerA;
    @Setter
    private ProblemResponseDTO.SelectProblemDTO problemForPlayerB;

    public void add(Long userId, String username, String roomPassword) {
        this.playerB = new Player(userId, username, 100);
    }

    public void startGame(List<Problem> problems, Long memberId) {
        roundProblems.clear();
        if (!this.hostId.equals(memberId)) { //방장이 아닐 때
            throw new BusinessException(memberId, "memberId", ErrorCode.USER_NOT_HOST);
        }
        if (playerA.getUserId() == null || playerB.getUserId() == null) { // 2명보다 작을 때
            throw new BusinessException(roomId, "roomId", ErrorCode.INSUFFICIENT_PLAYER);
        }
        if (problems == null || problems.isEmpty()) { // TODO : 문제에 대한 예외처리
            throw new BusinessException(roomId, "roomId", ErrorCode.GAME_ALREADY_STARTED);
        }
        int numberOfRounds = (int) Math.ceil((double) problems.size() / 3); // 안 나눠떨어질 때 예외처리
        for (int i = 0; i < numberOfRounds; i++) {
            int startIndex = i * 3;
            int endIndex = Math.min(startIndex + 3, problems.size());
            roundProblems.add(problems.subList(startIndex, endIndex));
        }
        this.isStart = true;
    }

    // 특정 라운드의 문제를 가져오는 메서드
    public List<Problem> getProblemsForRound(Integer roundNumber) {
        if (roundNumber < 0 || roundNumber >= roundProblems.size()) {
            throw new IndexOutOfBoundsException("Invalid round number: " + roundNumber);
        }
        return roundProblems.get(roundNumber);
    }

    public FightDTO updateHp(Long userId, int power) {
        if (playerA == null || playerB == null) {
            throw new IllegalArgumentException("Player not found");
        }
        if (playerA.getUserId().equals(userId)) {
            hasPlayerASubmitted = true;
            if(power != 0) updatePlayerHp(playerB, power);
        } else if (playerB.getUserId().equals(userId)) {
            hasPlayerBSubmitted = true;
            if(power != 0) updatePlayerHp(playerA, power);
        } else {
            throw new IllegalArgumentException("Invalid userId");
        }
        return FightDTO.builder().userId(userId).isAttack(this.isAttack).power(power).build();
    }

    private void updatePlayerHp(Player player, int power) {
        int adjustedPower = this.isAttack ? -power : power;
        if (isAttack) { // 이미 공격 한 경우
            if (playerA.getUserId().equals(player.getUserId())) player = playerB; // 내가 B
            else player = playerA; // 내가 A
            isAttack = false;
            power -= 10;
        } else if (!isAttack) { // 한번도 공격 안 한 경우
            this.isAttack = true;
        }
        player.setHp(player.getHp() - adjustedPower);
    }

    public Integer nextRound() {

        if (!Boolean.TRUE.equals(this.isStart)) {
            // TODO : 시작 안 된 상황
        }
        hasPlayerASubmitted = false;
        hasPlayerBSubmitted = false;
        this.isAttack = false;
        this.currentRound += 1;
        return currentRound;
    }

    public Player getPlayerById(Long userId) {
        if (playerA.getUserId().equals(userId)) {
            return playerA;
        } else if (playerB.getUserId().equals(userId)) {
            return playerB;
        } else {
            throw new IllegalArgumentException("Invalid userId");
        }
    }

    public Player getOpponentById(Long userId) {
        if (playerA.getUserId().equals(userId)) {
            return playerB;
        } else if (playerB.getUserId().equals(userId)) {
            return playerA;
        } else {
            throw new IllegalArgumentException("Invalid userId");
        }
    }

    public void leavePlayerB() {
        playerB = null;
    }

    public void initRoom() {
        playerA.setHp(100);
        playerB.setHp(100);
        this.isStart = false;
        this.hasPlayerASubmitted = false;
        this.hasPlayerBSubmitted = false;
        this.isAttack = false;
        this.currentRound = 0;
    }

    public boolean isRoomEmpty() {
        return playerA == null && playerB == null;
    }
}
