package com.scf.multi.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.multi.application.MultiGameService;
import com.scf.multi.domain.dto.room.RoomRequest.CreateRoomDTO;
import com.scf.multi.domain.dto.room.RoomResponse;
import com.scf.multi.domain.dto.problem.Problem;
import com.scf.multi.domain.dto.problem.ProblemAnswer;
import com.scf.multi.domain.dto.problem.ProblemChoice;
import com.scf.multi.domain.dto.problem.ProblemContent;
import com.scf.multi.domain.dto.problem.ProblemResponse;
import com.scf.multi.domain.dto.problem.ProblemType;
import com.scf.multi.domain.dto.user.Player;
import com.scf.multi.domain.model.MultiGameRoom;
import com.scf.multi.infrastructure.KafkaMessageProducer;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(MultiGameController.class)
@ExtendWith(MockitoExtension.class)
class MultiGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MultiGameService multiGameService;

    @MockBean
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private ObjectMapper objectMapper;

    private MultiGameRoom gameRoom;

    private List<Problem> problems;

    private Player player;

    @BeforeEach
    void setUp() {

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

        problems = Arrays.asList(problem1, problem2);

        player = Player.builder()
            .userId(1L)
            .username("testUser")
            .isHost(false)
            .build();

        gameRoom = MultiGameRoom.builder()
            .roomId(UUID.randomUUID().toString())
            .hostId(1L)
            .hostname("testUser")
            .title("Test Room")
            .maxPlayer(4)
            .password("1234")
            .build();
    }

    @Test
    @DisplayName("방 목록을 정상적으로 반환해야 한다.")
    void roomListTest() throws Exception {

        // Given
        RoomResponse.ListDTO listDTO = RoomResponse.ListDTO.builder()
            .roomId(gameRoom.getRoomId())
            .title(gameRoom.getTitle())
            .maxPlayer(gameRoom.getMaxPlayer())
            .hostname(gameRoom.getHostname())
            .curPlayer(gameRoom.getPlayers().size())
            .isLock(gameRoom.getPassword() != null)
            .isStart(gameRoom.getIsStart())
            .gameRound(gameRoom.getPlayRound())
            .build();
        List<RoomResponse.ListDTO> rooms = List.of(listDTO);
        when(multiGameService.findAllRooms()).thenReturn(rooms);

        // When & Then
        mockMvc.perform(get("/multi/room"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(rooms)));

        // Then
        verify(multiGameService, times(1)).findAllRooms();
    }

    @Test
    @DisplayName("특정 roomId에 대한 방 정보를 정상적으로 반환해야 한다.")
    void findRoomTest() throws Exception {

        // Given
        when(multiGameService.findOneById(gameRoom.getRoomId())).thenReturn(gameRoom);

        // When & Then
        mockMvc.perform(get("/multi/room/{roomId}", gameRoom.getRoomId()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(gameRoom)));

        // Then
        verify(multiGameService, times(1)).findOneById(gameRoom.getRoomId());
    }

    @Test
    @DisplayName("특정 roomId에 대한 사용자 목록을 정상적으로 반환해야 한다.")
    void userInRoomTest() throws Exception {

        // Given
        gameRoom.add(gameRoom.getPassword(), player);
        when(multiGameService.findOneById(gameRoom.getRoomId())).thenReturn(gameRoom);

        // When & Then
        mockMvc.perform(get("/multi/room/{roomId}/user", gameRoom.getRoomId()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(gameRoom.getPlayers())));

        // Then
        verify(multiGameService, times(1)).findOneById(gameRoom.getRoomId());
    }

    @Test
    @DisplayName("방을 성공적으로 생성해야 한다.")
    void createRoomTest() throws Exception {

        // Given
        CreateRoomDTO createRoomDTO = CreateRoomDTO.builder()
            .title("New Room")
            .maxPlayer(4)
            .password("password")
            .gameRound(5)
            .build();

        String generatedRoomId = "generated-room-id";
        when(multiGameService.createRoom(anyLong(), anyString(), eq(createRoomDTO))).thenReturn(generatedRoomId);

        // When & Then
        MvcResult result = mockMvc.perform(post("/multi/room")
                .header("memberId", 1L)
                .header("username", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRoomDTO)))
            .andExpect(status().isOk())
            .andReturn();

        // Then
        verify(multiGameService, times(1)).createRoom(1L, "testUser", createRoomDTO);
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(generatedRoomId);
    }

    @Test
    @DisplayName("사용자가 방에 성공적으로 참여해야 한다.")
    void joinRoomTest() throws Exception {

        // Given
        String roomId = "abc-def";
        String roomPassword = "1234";
        Long userId = 1L;
        String username = "testUser";

        // When & Then
        mockMvc.perform(post("/multi/room/{roomId}", roomId)
                .content(roomPassword)
                .contentType(MediaType.APPLICATION_JSON)
                .header("memberId", userId)
                .header("username", username))
            .andExpect(status().isOk());

        // Then
        verify(multiGameService, times(1)).joinRoom(roomId, roomPassword, userId, username);
    }

    @Test
    @DisplayName("방장은 게임을 성공적으로 시작해야 한다.")
    void gameStartTest() throws Exception {

        // Given
        String roomId = "abc-def";
        Long userId = 1L;

        List<ProblemResponse.ListDTO> problemList = problems.stream()
            .map(problem -> ProblemResponse.ListDTO.builder()
                .problemId(problem.getProblemId())
                .title(problem.getTitle())
                .problemType(problem.getProblemType())
                .category(problem.getCategory())
                .difficulty(problem.getDifficulty())
                .problemContent(problem.getProblemContent())
                .problemChoices(problem.getProblemChoices())
                .build())
            .toList();

        // When & Then
        mockMvc.perform(post("/multi/game/{roomId}/start", roomId)
                .header("memberId", userId))
            .andExpect(status().isOk());

        // Then
        verify(multiGameService, times(1)).startGame(roomId, userId);
    }
}

