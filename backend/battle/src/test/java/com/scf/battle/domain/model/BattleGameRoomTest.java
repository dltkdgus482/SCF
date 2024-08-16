package com.scf.battle.domain.model;

import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BattleGameRoomTest {

    private BattleGameRoom room;
    private Long hostId;
    private Player playerA;
    private Player playerB;

    @BeforeEach
    void setUp() {
        hostId = 1L;
        playerA = new Player(1L, "PlayerA", 100);
        playerB = new Player(null, "PlayerB", 100); // userId를 null로 설정

        room = BattleGameRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .hostId(hostId)
                .title("Test Room")
                .password("password")
                .playerA(playerA)
                .playerB(playerB)
                .isAttack(false)
                .isStart(false)
                .finalRound(3)
                .currentRound(0)
                .hasPlayerASubmitted(false)
                .hasPlayerBSubmitted(false)
                .build();
    }

    @Test
    void testAddPlayer() {
        room.add(2L, "PlayerB", "password");
        assertNotNull(room.getPlayerB());
        assertEquals(2L, room.getPlayerB().getUserId());
    }

    @Test
    void testStartGame() {
        room.getPlayerB().setUserId(2L); // playerB의 userId를 설정
        List<Problem> problems = Arrays.asList(new Problem(), new Problem(), new Problem(), new Problem(), new Problem(), new Problem());
        room.startGame(problems, hostId);
        assertTrue(room.getIsStart());
        assertEquals(2, room.getRoundProblems().size());
    }

    @Test
    void testStartGameNotHost() {
        room.getPlayerB().setUserId(2L); // playerB의 userId를 설정
        List<Problem> problems = Arrays.asList(new Problem(), new Problem(), new Problem(), new Problem(), new Problem(), new Problem());
        BusinessException exception = assertThrows(BusinessException.class, () -> room.startGame(problems, 2L));
        assertEquals(ErrorCode.USER_NOT_HOST.getMessage(), exception.getMessage());
    }

    @Test
    void testStartGameInsufficientPlayers() {
        List<Problem> problems = Arrays.asList(new Problem(), new Problem(), new Problem(), new Problem(), new Problem(), new Problem());
        BusinessException exception = assertThrows(BusinessException.class, () -> room.startGame(problems, hostId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCode.INSUFFICIENT_PLAYER.getMessage(), exception.getMessage());
    }

    @Test
    void testStartGameNoProblems() {
        room.getPlayerB().setUserId(2L); // playerB의 userId를 설정
        BusinessException exception = assertThrows(BusinessException.class, () -> room.startGame(null, hostId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorCode.GAME_ALREADY_STARTED.getMessage(), exception.getMessage());
    }

    @Test
    void testGetProblemsForRound() {
        List<Problem> round1Problems = Arrays.asList(new Problem(), new Problem(), new Problem());
        room.getRoundProblems().add(round1Problems);
        assertEquals(round1Problems, room.getProblemsForRound(0));
    }

    @Test
    void testGetProblemsForInvalidRound() {
        List<Problem> round1Problems = Arrays.asList(new Problem(), new Problem(), new Problem());
        room.getRoundProblems().add(round1Problems);
        assertThrows(IndexOutOfBoundsException.class, () -> room.getProblemsForRound(1));
    }

    @Test
    void testUpdateHp() {
        room.getPlayerB().setUserId(2L); // playerB의 userId를 설정
        room.updateHp(1L, 10);
        assertEquals(90, room.getPlayerB().getHp());
    }

    @Test
    void testNextRound() {
        room.getPlayerB().setUserId(2L); // playerB의 userId를 설정
        room = BattleGameRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .hostId(hostId)
                .title("Test Room")
                .password("password")
                .playerA(playerA)
                .playerB(new Player(2L, "PlayerB", 100))
                .isAttack(false)
                .isStart(true)
                .finalRound(3)
                .currentRound(0)
                .hasPlayerASubmitted(false)
                .hasPlayerBSubmitted(false)
                .build();
        int nextRound = room.nextRound();
        assertEquals(1, nextRound);
    }
}
