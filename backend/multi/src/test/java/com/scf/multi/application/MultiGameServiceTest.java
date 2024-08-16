package com.scf.multi.application;

import com.scf.multi.domain.dto.problem.ProblemResponse.ListDTO;
import com.scf.multi.domain.dto.room.RoomRequest.CreateRoomDTO;
import com.scf.multi.domain.dto.problem.Problem;
import com.scf.multi.domain.dto.problem.ProblemAnswer;
import com.scf.multi.domain.dto.problem.ProblemChoice;
import com.scf.multi.domain.dto.problem.ProblemContent;
import com.scf.multi.domain.dto.problem.ProblemResponse;
import com.scf.multi.domain.dto.problem.ProblemType;
import com.scf.multi.domain.dto.user.Player;
import com.scf.multi.domain.dto.user.Solved;
import com.scf.multi.domain.model.MultiGameRoom;
import com.scf.multi.domain.repository.MultiGameRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MultiGameServiceTest {

    @Mock
    private MultiGameRepository multiGameRepository;

    @Mock
    private ProblemService problemService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MultiGameService multiGameService;

    private MultiGameRoom room;

    private List<Problem> problems;

    private Player hostPlayer;

    private Player otherPlayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hostPlayer = Player.builder()
            .userId(1L)
            .username("Host")
            .isHost(true)
            .streakCount(2)
            .build();

        otherPlayer = Player.builder()
            .userId(2L)
            .username("Player2")
            .isHost(false)
            .streakCount(2)
            .build();

        room = MultiGameRoom.builder()
            .roomId(UUID.randomUUID().toString())
            .hostId(hostPlayer.getUserId())
            .hostname(hostPlayer.getUsername())
            .password("1234")
            .title("Test Room")
            .maxPlayer(4)
            .playRound(2)
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
            .problemType(ProblemType.SHORT_ANSWER_QUESTION)
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

        ProblemChoice correctChoice = ProblemChoice.builder()
            .problemId(3L)
            .choiceText(null)
            .choiceId(2)
            .build();

        Problem problem3 = Problem // 객관식
            .builder()
            .problemId(3L)
            .problemType(ProblemType.MULTIPLE_CHOICE)
            .category("정렬")
            .title("병합 정렬")
            .problemContent(ProblemContent.builder()
                .content("답을 고르세요.")
                .numberOfBlank(0)
                .problemId(3L)
                .build()
            )
            .problemChoices(problemChoices)
            .problemAnswers(List.of(ProblemAnswer.builder().answerId(1).problemId(3L).blankPosition(null).correctChoice(correctChoice).correctAnswerText(null).build()))
            .build();

        problems = Arrays.asList(problem1, problem2, problem3);
    }

    @Test
    @DisplayName("방을 생성할 수 있어야 한다.")
    void createRoomTest() {
        // Given
        CreateRoomDTO createRoomDTO = CreateRoomDTO.builder()
            .title("New Room")
            .maxPlayer(4)
            .password("password")
            .gameRound(3)
            .build();

        // When
        String roomId = multiGameService.createRoom(1L, "host", createRoomDTO);

        // Then
        verify(multiGameRepository).addRoom(any(MultiGameRoom.class));
        assertNotNull(roomId);
    }

    @Test
    @DisplayName("방에 참가할 수 있어야 한다.")
    void joinRoomTest() {
        // Given
        room.addPlayer("1234", hostPlayer);

        when(multiGameRepository.findOneById(anyString())).thenReturn(room);

        // When
        multiGameService.joinRoom(room.getRoomId(), "1234", 2L, "player2");

        // Then
        assertTrue(room.getPlayers().stream().anyMatch(p -> p.getUserId().equals(2L)));
    }

    @Test
    @DisplayName("빈칸 문제를 채점하고 점수를 옳바르게 계산해야 한다.")
    void markFillInTheBlankSolutionTest() {

        // Given
        room.addPlayer("1234", hostPlayer);
        room.addPlayer("1234", otherPlayer);
        room.gameStart(problems, room.getHostId());

        when(multiGameRepository.findOneById(anyString())).thenReturn(room);

        Solved solved = Solved.builder()
            .userId(1L)
            .solve(Map.of(1, 1, 2, 2, 3, 3))
            .solveText(null)
            .submitTime(20) // Within time limit
            .build();

        // When
        int score = multiGameService.markSolution(room.getRoomId(), solved);

        // Then
        assertTrue(score > 0);
    }

    @Test
    @DisplayName("주관식 문제를 채점하고 점수를 옳바르게 계산해야 한다.")
    void markShortAnswerQuestionSolutionTest() {

        // Given
        room.addPlayer("1234", hostPlayer);
        room.addPlayer("1234", otherPlayer);
        room.gameStart(problems, room.getHostId());
        room.nextRound(); // 2번 문제가 주관식

        when(multiGameRepository.findOneById(anyString())).thenReturn(room);

        Solved solved = Solved.builder()
            .userId(1L)
            .solve(null)
            .solveText("test answer")
            .submitTime(20) // Within time limit
            .build();

        // When
        int score = multiGameService.markSolution(room.getRoomId(), solved);

        // Then
        assertTrue(score > 0);
    }

    @Test
    @DisplayName("객관식 문제를 채점하고 점수를 옳바르게 계산해야 한다.")
    void markMultipleChoiceSolutionTest() {

        // Given
        room.addPlayer("1234", hostPlayer);
        room.addPlayer("1234", otherPlayer);
        room.gameStart(problems, room.getHostId());
        room.nextRound();
        room.nextRound(); // 3번 문제가 객관식

        when(multiGameRepository.findOneById(anyString())).thenReturn(room);

        Solved solved = Solved.builder()
            .userId(1L)
            .solve(Map.of(1, 2))
            .solveText(null)
            .submitTime(20) // Within time limit
            .build();

        // When
        int score = multiGameService.markSolution(room.getRoomId(), solved);

        // Then
        assertTrue(score > 0);
    }

    @Test
    @DisplayName("게임을 시작할 수 있어야 한다.")
    void startGameTest() {

        // Given
        room.addPlayer("1234", hostPlayer);
        room.addPlayer("1234", otherPlayer);
        when(multiGameRepository.findOneById(anyString())).thenReturn(room);
        when(problemService.getProblems(anyInt())).thenReturn(problems);

        // When
        multiGameService.startGame(room.getRoomId(), 1L);

        List<ListDTO> problemList = multiGameService.getProblems(room.getRoomId());

        // Then
        assertNotNull(problemList);
        assertEquals(3, problemList.size());
    }

    @Test
    @DisplayName("방을 삭제할 수 있어야 한다.")
    void deleteRoomTest() {

        // Given
        when(multiGameRepository.findOneById(anyString())).thenReturn(room);

        // When
        multiGameService.deleteRoom(room.getRoomId());

        // Then
        verify(multiGameRepository).deleteRoom(anyString());
    }
}
