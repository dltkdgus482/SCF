package com.scf.battle.application;

import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.Problem.ProblemAnswer;
import com.scf.battle.domain.dto.Problem.ProblemChoice;
import com.scf.battle.domain.dto.User.FightDTO;
import com.scf.battle.domain.dto.User.Solved;
import com.scf.battle.domain.enums.ProblemType;
import com.scf.battle.domain.model.BattleGameRoom;
import com.scf.battle.domain.repository.BattleGameRepository;
import com.scf.battle.global.error.ErrorCode;
import com.scf.battle.global.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private BattleGameRepository battleGameRepository;

    @Mock
    private ProblemService problemService;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startGame_roomNotFound() {
        // Given
        Long userId = 1L;
        String roomId = "room1";
        when(battleGameRepository.findById(roomId)).thenReturn(null);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.startGame(userId, roomId);
        });

        // Then
        assertEquals(ErrorCode.ROOM_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void startGame_gameAlreadyStarted() {
        // Given
        Long userId = 1L;
        String roomId = "room1";
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(room.getIsStart()).thenReturn(true);
        when(battleGameRepository.findById(roomId)).thenReturn(room);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.startGame(userId, roomId);
        });

        // Then
        assertEquals(ErrorCode.GAME_ALREADY_STARTED.getMessage(), exception.getMessage());
    }

    @Test
    void startGame_noProblemsFound() {
        // Given
        Long userId = 1L;
        String roomId = "room1";
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(room.getIsStart()).thenReturn(false);
        when(room.getFinalRound()).thenReturn(3);
        when(battleGameRepository.findById(roomId)).thenReturn(room);
        when(problemService.getProblems(9)).thenReturn(Collections.emptyList());

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.startGame(userId, roomId);
        });

        // Then
        assertEquals(ErrorCode.PROBLEM_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void startGame_success() {
        // Given
        Long userId = 1L;
        String roomId = "room1";
        BattleGameRoom room = mock(BattleGameRoom.class);
        List<Problem> problems = List.of(mock(Problem.class));
        when(room.getIsStart()).thenReturn(false);
        when(room.getFinalRound()).thenReturn(3);
        when(battleGameRepository.findById(roomId)).thenReturn(room);
        when(problemService.getProblems(9)).thenReturn(problems);

        // When
        List<Problem> result = gameService.startGame(userId, roomId);

        // Then
        assertEquals(problems, result);
        verify(room).startGame(problems, userId);
    }

    @Test
    void markSolution_roomNotFound() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        Solved solved = mock(Solved.class);
        when(battleGameRepository.findById(roomId)).thenReturn(null);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.markSolution(roomId, userId, solved);
        });

        // Then
        assertEquals(ErrorCode.ROOM_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void markSolution_noProblemsForRound() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        Solved solved = mock(Solved.class);
        BattleGameRoom room = mock(BattleGameRoom.class);
        when(solved.getRound()).thenReturn(1);
        when(battleGameRepository.findById(roomId)).thenReturn(room);
        when(room.getProblemsForRound(1)).thenReturn(Collections.emptyList());

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.markSolution(roomId, userId, solved);
        });

        // Then
        assertEquals(ErrorCode.NO_PROBLEM_FOR_ROUND.getMessage(), exception.getMessage());
    }

    @Test
    void markSolution_problemNotFound() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        Solved solved = mock(Solved.class);
        BattleGameRoom room = mock(BattleGameRoom.class);
        Problem problem = mock(Problem.class);
        when(solved.getRound()).thenReturn(1);
        when(solved.getProblemId()).thenReturn(1L);
        when(battleGameRepository.findById(roomId)).thenReturn(room);
        when(room.getProblemsForRound(1)).thenReturn(List.of(problem));
        when(problem.getProblemId()).thenReturn(2L); // 다른 ID를 반환

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            gameService.markSolution(roomId, userId, solved);
        });

        // Then
        assertEquals(ErrorCode.PROBLEM_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void markSolution_success() {
        // Given
        String roomId = "room1";
        Long userId = 1L;
        Solved solved = mock(Solved.class);
        BattleGameRoom room = mock(BattleGameRoom.class);
        Problem problem = mock(Problem.class);
        ProblemChoice correctChoice = ProblemChoice.builder().choiceId(1).build();
        ProblemAnswer problemAnswer = ProblemAnswer.builder().correctChoice(correctChoice).build();
        List<ProblemAnswer> problemAnswers = List.of(problemAnswer);

        when(solved.getRound()).thenReturn(1);
        when(solved.getProblemId()).thenReturn(1L);
        when(solved.getSolve()).thenReturn(Collections.singletonMap(1, 1)); // 정답으로 설정
        when(battleGameRepository.findById(roomId)).thenReturn(room);
        when(room.getProblemsForRound(1)).thenReturn(List.of(problem));
        when(problem.getProblemId()).thenReturn(1L);
        when(problem.getProblemType()).thenReturn(ProblemType.MULTIPLE_CHOICE);
        when(problem.getProblemAnswers()).thenReturn(problemAnswers);
        when(room.updateHp(anyLong(), anyInt())).thenReturn(mock(FightDTO.class));

        // When
        FightDTO result = gameService.markSolution(roomId, userId, solved);

        // Then
        assertNotNull(result);
    }
}
