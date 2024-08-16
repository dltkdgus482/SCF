package com.scf.multi.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scf.multi.domain.dto.problem.Problem;
import com.scf.multi.domain.dto.problem.ProblemAnswer;
import com.scf.multi.domain.dto.problem.ProblemChoice;
import com.scf.multi.domain.dto.problem.ProblemContent;
import com.scf.multi.domain.dto.problem.ProblemType;
import com.scf.multi.domain.dto.user.Player;
import com.scf.multi.domain.dto.user.Rank;
import com.scf.multi.global.error.ErrorCode;
import com.scf.multi.global.error.exception.BusinessException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiGameRoomTest {

    private MultiGameRoom gameRoom;
    private Player hostPlayer;
    private Player otherPlayer;
    private List<Problem> problems;

    @BeforeEach
    public void setUp() {
        hostPlayer = Player.builder()
            .userId(1L)
            .username("Host")
            .isHost(true)
            .build();

        otherPlayer = Player.builder()
            .userId(2L)
            .username("Player2")
            .isHost(false)
            .build();

        List<ProblemChoice> problemChoices = Arrays.asList(
            ProblemChoice.builder().problemId(1L).choiceId(1).choiceText("i+1").build(),
            ProblemChoice.builder().problemId(1L).choiceId(2).choiceText("i+2").build(),
            ProblemChoice.builder().problemId(1L).choiceId(3).choiceText("i+3").build()
        );

        List<ProblemAnswer> ProblemAnswers = Arrays.asList(
            ProblemAnswer.builder().answerId(1).problemId(1L).blankPosition(1).correctChoice(problemChoices.get(0)).correctAnswerText(null).build(),
            ProblemAnswer.builder().answerId(2).problemId(1L).blankPosition(2).correctChoice(problemChoices.get(1)).correctAnswerText(null).build(),
            ProblemAnswer.builder().answerId(3).problemId(1L).blankPosition(3).correctChoice(problemChoices.get(2)).correctAnswerText(null).build()
        );

        Problem problem1 = Problem // 빈칸 채우기
            .builder()
            .problemId(1L)
            .problemType(ProblemType.FILL_IN_THE_BLANK)
            .category("정렬")
            .title("버블 정렬")
            .problemContent(ProblemContent.builder()
                .content("빈칸을 채우세요.")
                .numberOfBlank(3)
                .problemId(1L)
                .build()
            )
            .problemChoices(problemChoices)
            .problemAnswers(ProblemAnswers)
            .build();

        Problem problem2 = Problem // 주관식 
            .builder()
            .problemId(2L)
            .problemType(ProblemType.FILL_IN_THE_BLANK)
            .category("정렬")
            .title("삽입 정렬")
            .problemContent(ProblemContent.builder()
                .content("답을 쓰세요.")
                .numberOfBlank(0)
                .problemId(2L)
                .build()
            )
            .problemChoices(null)
            .problemAnswers(List.of(ProblemAnswer.builder().answerId(2).problemId(2L).blankPosition(null).correctChoice(null).correctAnswerText("test answer").build()))
            .build();

        problems = Arrays.asList(problem1, problem2);

        gameRoom = MultiGameRoom.builder()
            .roomId("Room1")
            .hostId(hostPlayer.getUserId())
            .title("Test Room")
            .password("secret")
            .maxPlayer(3)
            .build();
    }

    @Test
    @DisplayName("방 비밀번호가 일치할 경우 방에 플레이어가 정상적으로 추가되어야 한다. (1명)")
    void AddPlayerWithCorrectPasswordTest() {

        // given
        String roomPassword = "secret";

        // when
        gameRoom.add(roomPassword, hostPlayer);

        // then
        List<Player> players = gameRoom.getPlayers();

        assertThat(players).hasSize(1);
        assertThat(players.get(0)).isEqualTo(hostPlayer);
        assertEquals("Host", gameRoom.getPlayers().get(0).getUsername());
        assertEquals(0, gameRoom.getScoreBoard().get(hostPlayer.getUserId()));
    }

    @Test
    @DisplayName("방 비밀번호가 일치할 경우 방에 플레이어가 정상적으로 추가되어야 한다. (2명)")
    void AddPlayerWithCorrectPasswordTest2() {

        // given
        String roomPassword = "secret";

        // when
        gameRoom.add(roomPassword, hostPlayer);
        gameRoom.add(roomPassword, otherPlayer);

        // then
        List<Player> players = gameRoom.getPlayers();

        assertThat(players).hasSize(2);
        assertThat(players.get(0)).isEqualTo(hostPlayer);
        assertThat(players.get(1)).isEqualTo(otherPlayer);
        assertEquals("Player2", gameRoom.getPlayers().get(1).getUsername());
        assertEquals(0, gameRoom.getScoreBoard().get(hostPlayer.getUserId()));
        assertEquals(0, gameRoom.getScoreBoard().get(otherPlayer.getUserId()));
    }

    @Test
    @DisplayName("방 비밀번호가 틀릴 경우 정상적으로 예외가 발생해야 한다.")
    void AddPlayerWithWrongPasswordTest() {

        // given
        String roomPassword = "wrong password";
        gameRoom.add("secret", hostPlayer);

        // when
        BusinessException businessException = assertThrows(BusinessException.class,
            () -> gameRoom.add(roomPassword, otherPlayer));

        // then
        assertThat(businessException.getMessage()).isEqualTo(
            ErrorCode.PASSWORD_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("방 인원이 가득 찼을 경우 정상적으로 예외가 발생해야 한다.")
    void AddPlayerWithMaxPlayerTest() {

        // given
        String roomPassword = "secret";
        gameRoom.add(roomPassword, hostPlayer);
        gameRoom.add(roomPassword, otherPlayer);
        Player player3 = Player.builder()
            .userId(3L)
            .username("Player3")
            .isHost(false)
            .build();
        gameRoom.add(roomPassword, player3);

        // when
        Player player4 = Player.builder()
            .userId(4L)
            .username("Player4")
            .isHost(false)
            .build();

        BusinessException businessException = assertThrows(BusinessException.class,
            () -> gameRoom.add(roomPassword, player4));

        // then
        assertThat(businessException.getMessage()).isEqualTo(
            ErrorCode.MAX_PLAYERS_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("게임이 시작된 후 추가하려는 경우 정상적으로 예외가 발생해야 한다.")
    void AddPlayerWithAlreadyStartGameTest() {

        // given
        String roomPassword = "secret";
        gameRoom.add(roomPassword, hostPlayer);
        gameRoom.add(roomPassword, otherPlayer);
        gameRoom.gameStart(problems, hostPlayer.getUserId());
        Player player3 = Player.builder()
            .userId(3L)
            .username("Player3")
            .isHost(false)
            .build();

        // when
        BusinessException businessException = assertThrows(BusinessException.class,
            () -> gameRoom.add(roomPassword, player3));

        // then
        assertThat(businessException.getMessage()).isEqualTo(
            ErrorCode.GAME_ALREADY_STARTED.getMessage());
    }

    @Test
    @DisplayName("방에서 플레이어가 정상적으로 제거되어야 한다.")
    void RemovePlayerTest() {

        // given
        String roomPassword = "secret";
        gameRoom.add(roomPassword, hostPlayer);

        // when
        gameRoom.remove(1L);

        // then
        assertTrue(gameRoom.getPlayers().isEmpty());
    }

    @Test
    @DisplayName("방에 포함되지 않은 유저를 제거하면 정상적으로 예외가 발생해야 한다.")
    void RemovePlayerNonExistentTest() {

        // given
        String roomPassword = "secret";
        gameRoom.add(roomPassword, hostPlayer);

        // when
        BusinessException businessException = assertThrows(BusinessException.class,
            () -> gameRoom.remove(2L));

        // then
        assertThat(businessException.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게임을 시작하기 위한 상태가 갖춰져야 한다.")
    void GameStartWithValidConditionsTest() {

        // given
        gameRoom.add("secret", hostPlayer);
        gameRoom.add("secret", otherPlayer);

        // when
        gameRoom.gameStart(problems, hostPlayer.getUserId());

        // then
        assertThat(gameRoom.getProblems()).isNotEmpty();
        assertThat(gameRoom.getProblems()).hasSize(2);
        assertThat(gameRoom.getIsStart()).isTrue();
    }

    @Test
    @DisplayName("방장이 아닌 유저가 게임을 시작하면 정상적으로 예외가 발생해야 한다.")
    public void GameStartWithNonHostUserTest() {

        // given
        gameRoom.add("secret", hostPlayer);
        gameRoom.add("secret", otherPlayer);

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameRoom.gameStart(problems, otherPlayer.getUserId());
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorCode.USER_NOT_HOST.getMessage());
    }

    @Test
    @DisplayName("최소 플레이어 수가 충족되지 않을 경우 정상적으로 예외가 발생해야 한다.")
    public void GameStartWithInsufficientPlayersTest() {

        // given
        gameRoom.add("secret", hostPlayer);

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameRoom.gameStart(problems, hostPlayer.getUserId());
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INSUFFICIENT_PLAYER.getMessage());
    }


    @Test
    @DisplayName("게임 시작 후 문제가 준비되지 않을 경우 정상적으로 예외가 발생해야 한다.")
    public void testGameStartWithNoProblems() {

        // given
        gameRoom.add("secret", hostPlayer);
        gameRoom.add("secret", otherPlayer);

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameRoom.gameStart(null, hostPlayer.getUserId());
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROBLEM.getMessage());
    }

    @Test
    @DisplayName("플레이어에 대한 점수가 정상적으로 업데이트 되어야 한다.")
    void UpdateScoreTest() {

        // given
        gameRoom.add("secret", hostPlayer);

        // when
        gameRoom.updateScoreBoard(hostPlayer.getUserId(), 10);

        // then
        assertThat(gameRoom.getScoreBoard().get(hostPlayer.getUserId())).isEqualTo(10);
    }

    @Test
    @DisplayName("존재하지 않는 유저에 대해 점수를 업데이트 하려고 하면 정상적으로 예외가 발생해야 한다.")
    public void UpdateScoreForNonExistentPlayerTest() {

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameRoom.updateScoreBoard(otherPlayer.getUserId(), 10);
        });

        // then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("라운드가 정상적으로 증가해야 한다.")
    void NextRoundTest() {

        // given
        gameRoom.add("secret", hostPlayer);
        gameRoom.add("secret", otherPlayer);
        gameRoom.gameStart(problems, hostPlayer.getUserId());

        // when
        gameRoom.nextRound();

        // then
        assertThat(gameRoom.getRound()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임이 시작 되기 전에 라운드가 증가되면 정상적으로 예외가 발생해야 한다.")
    public void testNextRoundBeforeGameStart() {

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameRoom.nextRound();
        });

        assertThat(exception.getMessage()).isEqualTo(ErrorCode.GAME_NOT_START.getMessage());
    }

    @Test
    @DisplayName("순위가 정상적으로 계산되어야 한다.")
    void CalculateRankTest() {

        // given
        gameRoom.add("secret", hostPlayer);
        gameRoom.add("secret", otherPlayer);

        gameRoom.updateLeaderBoard(hostPlayer.getUserId(), 10);
        gameRoom.updateLeaderBoard(otherPlayer.getUserId(), 15);

        // when
        List<Rank> ranks = gameRoom.getGameRank();
        System.out.println(ranks.toString());

        // then
        assertThat(ranks).hasSize(2);
        assertThat(ranks.get(0).getUserId()).isEqualTo(otherPlayer.getUserId());
        assertThat(ranks.get(1).getUserId()).isEqualTo(hostPlayer.getUserId());
    }
}
