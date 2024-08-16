package com.scf.battle.application;

import com.scf.battle.domain.dto.Problem.Problem;
import com.scf.battle.domain.dto.Problem.ProblemRequestDTO;
import com.scf.battle.domain.dto.Problem.ProblemResponseDTO;
import com.scf.battle.domain.dto.User.Player;
import com.scf.battle.domain.dto.User.Solved;
import com.scf.battle.domain.enums.ProblemType;
import com.scf.battle.domain.model.BattleGameRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleSocketServiceTest {

    @Mock
    private KafkaService kafkaService;

    @Mock
    private RoomService roomService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private BattleSocketService battleSocketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoundSelectProblems_success() {
        // Given
        BattleGameRoom room = mock(BattleGameRoom.class);
        Problem problem = mock(Problem.class);
        when(room.getProblemsForRound(anyInt())).thenReturn(List.of(problem));
        when(problem.getProblemId()).thenReturn(1L);
        when(problem.getTitle()).thenReturn("title");
        when(problem.getProblemType()).thenReturn(ProblemType.MULTIPLE_CHOICE);
        when(problem.getCategory()).thenReturn("category");
        when(problem.getDifficulty()).thenReturn(1);
        when(problem.getProblemContent()).thenReturn(null);
        when(problem.getProblemChoices()).thenReturn(null);

        // When
        List<ProblemResponseDTO.SelectProblemDTO> result = battleSocketService.getRoundSelectProblems(room);

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProblemId());
        assertEquals("title", result.get(0).getTitle());
        assertEquals(ProblemType.MULTIPLE_CHOICE, result.get(0).getProblemType());
        assertEquals("category", result.get(0).getCategory());
        assertEquals(1, result.get(0).getDifficulty());
    }

    @Test
    void findSelectedProblem_success() {
        // Given
        ProblemResponseDTO.SelectProblemDTO problem = ProblemResponseDTO.SelectProblemDTO.builder()
                .problemId(1L)
                .build();
        List<ProblemResponseDTO.SelectProblemDTO> problems = List.of(problem);

        // When
        Optional<ProblemResponseDTO.SelectProblemDTO> result = battleSocketService.findSelectedProblem(problems, 1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getProblemId());
    }

    @Test
    void assignProblemToOpponent_success() {
        // Given
        BattleGameRoom room = mock(BattleGameRoom.class);
        Player selectingUser = mock(Player.class);
        ProblemResponseDTO.SelectProblemDTO problem = mock(ProblemResponseDTO.SelectProblemDTO.class);
        when(selectingUser.getUserId()).thenReturn(1L);
        when(room.getPlayerA()).thenReturn(selectingUser);

        // When
        battleSocketService.assignProblemToOpponent(room, selectingUser, problem);

        // Then
        verify(room).setProblemForPlayerB(problem);
    }

    @Test
    void updatePlayerSolvedList_success() {
        // Given
        BattleGameRoom room = mock(BattleGameRoom.class);
        Solved solved = mock(Solved.class);
        Player playerA = mock(Player.class);
        when(solved.getUserId()).thenReturn(1L);
        when(room.getPlayerA()).thenReturn(playerA);
        when(playerA.getUserId()).thenReturn(1L);

        // When

        // Then
        verify(playerA).addSolved(solved);
    }



    @Test
    void leaveRoom_notHost() {
        // Given
        Long memberId = 2L;
        String roomId = "room1";
        BattleGameRoom room = mock(BattleGameRoom.class);
        Player playerA = mock(Player.class);
        Player playerB = mock(Player.class);
        when(roomService.findById(roomId)).thenReturn(room);
        when(room.getHostId()).thenReturn(1L);
        when(room.getPlayerA()).thenReturn(playerA);
        when(room.getPlayerB()).thenReturn(playerB);

        // When
        battleSocketService.leaveRoom(memberId, roomId);

        // Then
        verify(room, times(1)).leavePlayerB();
    }

    @Test
    void buildSolved_success() {
        // Given
        ProblemRequestDTO problemRequestDTO = mock(ProblemRequestDTO.class);
        when(problemRequestDTO.getProblemId()).thenReturn(1L);
        when(problemRequestDTO.getUserId()).thenReturn(1L);
        when(problemRequestDTO.getSolve()).thenReturn(Map.of(1, 1));
        when(problemRequestDTO.getSolveText()).thenReturn("solveText");
        when(problemRequestDTO.getSubmitTime()).thenReturn(1);
        when(problemRequestDTO.getRound()).thenReturn(1);

        // When
       // Solved result = battleSocketService.buildSolved(problemRequestDTO);

        // Then
//        assertEquals(1L, result.getProblemId());
//        assertEquals(1L, result.getUserId());
//        assertEquals(Map.of(1, 1), result.getSolve());
//        assertEquals("solveText", result.getSolveText());
//        assertEquals(1, result.getSubmitTime());
//        assertEquals(1, result.getRound());
    }
}
