package com.scf.multi.presentation.websocket;

import com.scf.multi.application.MultiGameService;
import com.scf.multi.domain.dto.problem.ProblemResponse.ListDTO;
import com.scf.multi.domain.dto.user.PlayerListDTO;
import com.scf.multi.domain.dto.user.SubmitItem;
import com.scf.multi.domain.event.GameStartedEvent;
import com.scf.multi.domain.dto.socket_message.request.SolvedMessage;
import com.scf.multi.domain.dto.socket_message.response.ResponseMessage;
import com.scf.multi.domain.dto.user.Player;
import com.scf.multi.domain.dto.user.Rank;
import com.scf.multi.domain.dto.user.Solved;
import com.scf.multi.global.error.exception.BusinessException;
import com.scf.multi.global.utils.JsonConverter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiGameWebSocketHandler extends TextWebSocketHandler {

    private final MultiGameService multiGameService;
    private final static Map<String, String> rooms = new ConcurrentHashMap<>(); // session ID -> room ID (유저가 어떤 방에 연결됐는지 알려고)
    private final static Map<String, Set<WebSocketSession>> sessionRooms = new ConcurrentHashMap<>(); // room ID -> sessions (방에 연결된 유저들을 알려고)
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @EventListener
    public void onGameStarted(GameStartedEvent event) throws Exception {

        String roomId = event.getRoomId();
        List<ListDTO> problems = multiGameService.getProblems(roomId);
        broadcastMessageToRoom(roomId, "gameStart", problems);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String roomId = (String) session.getAttributes().get("roomId");
        Long userId = (Long) session.getAttributes().get("userId");

        multiGameService.validatePlayerToRoom(roomId, userId);

        Player connectedPlayer = multiGameService.connectPlayer(roomId, userId, session.getId());

        addPlayerSessionToRoom(session, roomId);

        broadcastMessageToRoom(roomId, "notice",
            connectedPlayer.getUsername() + " 님이 게임에 참가 하였습니다.");

        List<PlayerListDTO> playerList = multiGameService.getPlayerList(roomId);
        for(PlayerListDTO playerListDTO : playerList) {
            log.debug("afterConnectionEstablished player name: {}", playerListDTO.getUsername());
        }
        broadcastMessageToRoom(roomId, "player-list", playerList);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage)
        throws Exception {

        String roomId = rooms.get(session.getId());

        multiGameService.validateRoomIsStart(roomId);

        SolvedMessage solvedMessage = getSolvedMessage(textMessage);

        Solved solved = multiGameService.makeSolved(roomId, session.getId(),
            solvedMessage.getContent());

        int attainedScore = multiGameService.markSolution(roomId, solved); // 문제 채점
        multiGameService.updateScoreBoard(roomId, session.getId(), attainedScore);
        multiGameService.updateLeaderBoard(roomId, session.getId(), attainedScore);

        String attainScoreMessage = makeResponseMessage("attainScore", attainedScore);
        sendMessage(session, attainScoreMessage);

        multiGameService.changeSubmitItem(roomId, session.getId(), true);
        List<SubmitItem> submits = multiGameService.getSubmits(roomId);
        broadcastMessageToRoom(roomId, "submit-list", submits);

        multiGameService.increaseSubmit(roomId);

        boolean isAllPlayerSubmit = multiGameService.checkIsAllPlayerSubmit(roomId);

        if(isAllPlayerSubmit) {
            handleRoundCompletion(roomId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception {

        String roomId = rooms.get(session.getId());

        Player exitPlayer = multiGameService.handlePlayerExit(roomId, session.getId());

        removePlayerFromRoom(session, roomId);

        broadcastMessageToRoom(roomId, "notice", exitPlayer.getUsername() + "님이 게임을 나갔습니다.");

        List<PlayerListDTO> playerList = multiGameService.getPlayerList(roomId);
        broadcastMessageToRoom(roomId, "player-list", playerList);

        hostRotateIfNecessary(roomId, exitPlayer);

        log.debug("roomId: {}", rooms.get(session.getId()));

        boolean isAllPlayerSubmit = multiGameService.checkIsAllPlayerSubmit(roomId); // 게임중 유저가 나갈 경우, 참여한 모든 플레이어가 제출했는지 확인

        if (isAllPlayerSubmit) { // 모든 플레이어가 제출했으면
            handleRoundCompletion(roomId); // 다음 라운드 진행
        }

        // 마지막 유저가 방을 나간 후 3초 후에 방을 삭제하는 작업을 스케줄링
        Set<WebSocketSession> roomSessions = sessionRooms.get(roomId);
        if (roomSessions == null || roomSessions.isEmpty()) {
            executorService.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3); // 3초간 재접속을 기다림
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                checkAndDeleteRoom(roomId); // 3초 후 연결 상태를 확인하고, 연결된 세션이 없으면 방 제거
            });
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
        throws Exception {
        // 예외 처리 로직
        log.error("Transport error for session {}: {}", session.getId(), exception.getMessage());
        if (exception instanceof BusinessException e) {
            session.sendMessage(new TextMessage(
                "[Error]: " + e.getMessage() + " " + e.getFieldName() + " : "
                    + e.getInvalidValue()));
        } else {
            super.handleTransportError(session, exception);
        }
    }

    private void sendMessage(WebSocketSession session, String attainScoreMessage)
        throws IOException {
        session.sendMessage(new TextMessage(attainScoreMessage));
    }

    private void broadcastMessageToRoom(String roomId, String type, Object payload)
        throws Exception {

        Set<WebSocketSession> roomSessions = sessionRooms.get(roomId);

        if (roomSessions != null) {

            String message = makeResponseMessage(type, payload);

            for (WebSocketSession session : roomSessions) {
                if (session.isOpen()) {
                    sendMessage(session, message);
                }
            }
        }
    }

    private static void addPlayerSessionToRoom(WebSocketSession session, String roomId) {
        rooms.put(session.getId(), roomId);
        sessionRooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    private static SolvedMessage getSolvedMessage(TextMessage textMessage) throws IOException {

        String msg = textMessage.getPayload();
        return JsonConverter.getInstance().toObject(msg, SolvedMessage.class);
    }

    private static String makeResponseMessage(String type, Object payload) throws IOException {
        ResponseMessage responseMessage = ResponseMessage.builder().type(type).payload(payload)
            .build();
        return JsonConverter.getInstance().toString(responseMessage);
    }

    private void hostRotateIfNecessary(String roomId, Player exitPlayer) throws Exception {
        if (exitPlayer.getIsHost() && sessionRooms.get(roomId) != null && !sessionRooms.get(roomId).isEmpty()) {

            Player newHost = multiGameService.rotateHost(roomId);

            broadcastMessageToRoom(roomId, "newHost", newHost.getUserId()); // 새로운 방장 broadcasting
        }
    }

    private void removePlayerFromRoom(WebSocketSession session, String roomId) {
        Set<WebSocketSession> roomSessions = sessionRooms.get(roomId);
        if (roomSessions != null) {

            roomSessions.remove(session); // 나간 유저 방에서 삭제

            if (roomSessions.isEmpty()) { // 방에 포함된 마지막 유저가 나갔을 경우 방을 삭제
                sessionRooms.remove(roomId);
            }
        }
    }

    private void handleRoundCompletion(String roomId) throws Exception {

        multiGameService.processRound(roomId);

        multiGameService.saveSolved(roomId);

        deliveryRoundRank(roomId);

        deliveryGameRank(roomId);

        if (multiGameService.checkIsFinishGame(roomId)) { // 게임이 끝났으면

            boolean isFinishGame = multiGameService.checkIsFinishGame(roomId);

            if (isFinishGame) {
                multiGameService.finalizeGame(roomId);
            }
        }

        multiGameService.finishRound(roomId);
    }

    private void deliveryGameRank(String roomId) throws Exception {
        List<Rank> gameRank = multiGameService.getGameRank(roomId);
        broadcastMessageToRoom(roomId, "gameRank", gameRank);
    }

    private void deliveryRoundRank(String roomId) throws Exception {
        List<Rank> roundRank = multiGameService.getRoundRank(roomId);
        broadcastMessageToRoom(roomId, "roundRank", roundRank);
    }

    private void checkAndDeleteRoom(String roomId) {
        Set<WebSocketSession> roomSessions = sessionRooms.get(roomId);
        if (roomSessions == null || roomSessions.isEmpty()) {
            multiGameService.deleteRoom(roomId);
        }
    }
}
