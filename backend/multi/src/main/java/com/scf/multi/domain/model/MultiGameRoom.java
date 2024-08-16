package com.scf.multi.domain.model;

import com.scf.multi.domain.dto.problem.Problem;
import com.scf.multi.domain.dto.user.Player;
import com.scf.multi.domain.dto.user.Rank;
import com.scf.multi.domain.dto.user.SubmitItem;
import com.scf.multi.global.error.ErrorCode;
import com.scf.multi.global.error.exception.BusinessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MultiGameRoom {

    private final String roomId;
    private Long hostId;
    private String hostname;
    private final String title;
    private final String password;
    private final Integer maxPlayer;
    private final Integer playRound;

    private final List<Problem> problems = new ArrayList<>();
    private final List<Player> players = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Integer> leaderBoard = Collections.synchronizedMap(new HashMap<>());
    private final Map<Long, Integer> scoreBoard = Collections.synchronizedMap(new HashMap<>());

    private Boolean isStart;
    private Integer round;
    private final AtomicInteger curSubmitCount;
    private final List<SubmitItem> submits = new ArrayList<>();

    @Builder
    public MultiGameRoom(String roomId, Long hostId, String hostname, String title, String password, Integer maxPlayer, Integer playRound) {
        this.roomId = roomId;
        this.hostId = hostId;
        this.hostname = hostname;
        this.title = title;
        this.password = password;
        this.maxPlayer = maxPlayer;

        this.playRound = playRound;
        this.isStart = false;
        this.round = 0;
        this.curSubmitCount = new AtomicInteger(0);
    }

    public List<Player> getPlayers() {
        // 읽기 전용 List 반환
        return Collections.unmodifiableList(players);
    }

    public List<Problem> getProblems() {
        // 읽기 전용 List 반환
        return Collections.unmodifiableList(this.problems);
    }

    public void addPlayer(String roomPassword, Player player) {

        if (this.password != null && !this.password.equals(roomPassword)) {
            throw new BusinessException(this.password, "roomPassword", ErrorCode.PASSWORD_MISMATCH);
        }

        if (this.isStart) {
            throw new BusinessException(roomId, "roomId", ErrorCode.GAME_ALREADY_STARTED);
        }

        if (this.players.size() >= this.maxPlayer) {
            throw new BusinessException(this.players.size(), "room max player: " + this.maxPlayer,
                ErrorCode.MAX_PLAYERS_EXCEEDED);
        }

        for (Player p : players) { // 한 번 들어온적 있는 유저인지 확인
            if (p.getUserId().equals(player.getUserId())) {
                p.setIsOnRoom(true);
                return;
            }
        }

        this.players.add(player); // 처음 들어온 유저이면 player 목록에 추가
    }

    public void remove(Long userId) {

        boolean hasPlayer = this.players.stream()
            .anyMatch(player -> player.getUserId().equals(userId));

        if (!hasPlayer || !this.scoreBoard.containsKey(userId)) {
            throw new BusinessException(userId, "userId", ErrorCode.USER_NOT_FOUND);
        }

        this.players.removeIf(player -> player.getUserId().equals(userId));
    }

    public void gameStart(List<Problem> problems, Long userId) {

        if (!userId.equals(hostId)) { // 방장이 아닐 경우, 게임 시작 불가능
            throw new BusinessException(userId, "userId", ErrorCode.USER_NOT_HOST);
        }

        if (this.players.size() < 2) { // 예를 들어, 최소 2명의 플레이어가 있어야 한다고 가정
            throw new BusinessException(players.size(), "참가자 인원", ErrorCode.INSUFFICIENT_PLAYER);
        }

        if (problems == null || problems.isEmpty()) {
            throw new BusinessException(null, "문제", ErrorCode.INVALID_PROBLEM);
        }

        readyGameState(problems);
    }

    public void updateLeaderBoard(Long userId, int score) {

        boolean hasPlayer = this.players.stream()
            .filter(player -> player.getIsOnRoom().equals(true))
            .anyMatch(player -> player.getUserId().equals(userId));

        if (!hasPlayer || !this.scoreBoard.containsKey(userId)) {
            return;
        }

        int newScore = leaderBoard.get(userId) + score;
        this.leaderBoard.put(userId, newScore);
    }

    public void updateScoreBoard(Long userId, int score) {

        boolean hasPlayer = this.players.stream()
            .filter(player -> player.getIsOnRoom().equals(true))
            .anyMatch(player -> player.getUserId().equals(userId));

        if (!hasPlayer || !this.scoreBoard.containsKey(userId)) {
            return;
        }

        this.scoreBoard.put(userId, score);
    }

    public void nextRound() {

        if (!Boolean.TRUE.equals(this.isStart)) {
            throw new BusinessException(isStart, "isStart", ErrorCode.GAME_NOT_START);
        }

        this.round += 1;
    }

    private String getUsernameByUserId(Long userId) {
        return players.stream()
            .filter(player -> player.getIsOnRoom().equals(true))
            .filter(player -> player.getUserId().equals(userId))
            .map(Player::getUsername)
            .findFirst()
            .orElse("Unknown");
    }

    public List<Rank> getGameRank() {

        List<Rank> ranks = leaderBoard.entrySet().stream()
            .map(entry -> {
                Long userId = entry.getKey();
                String username = getUsernameByUserId(userId);

                return Rank.builder()
                    .userId(userId)
                    .username(username)
                    .score(entry.getValue())
                    .build();
            })
            .filter(rank -> !rank.getUsername().equals("Unknown")) // 유효한 Rank 객체만 필터링
            .sorted(Comparator.comparingInt(Rank::getScore).reversed()) // 점수 기준으로 내림차순 정렬
            .collect(Collectors.toList());

        AtomicInteger rankCounter = new AtomicInteger(1);
        ranks.forEach(rank -> rank.setRank(rankCounter.getAndIncrement()));

        return ranks;
    }

    public List<Rank> getRoundRank() {

        List<Rank> ranks = scoreBoard.entrySet().stream()
            .map(entry -> {
                Long userId = entry.getKey();
                String username = getUsernameByUserId(userId);

                return Rank.builder()
                    .userId(userId)
                    .username(username)
                    .score(entry.getValue())
                    .build();
            })
            .filter(rank -> !rank.getUsername().equals("Unknown")) // 유효한 Rank 객체만 필터링
            .sorted(Comparator.comparingInt(Rank::getScore).reversed()) // 점수 기준으로 내림차순 정렬
            .limit(3) // 상위 3개의 순위로 제한
            .collect(Collectors.toList());


        AtomicInteger rankCounter = new AtomicInteger(1);
        ranks.forEach(rank -> rank.setRank(rankCounter.getAndIncrement()));

        return ranks;
    }

    public void exitRoom(Player exitPlayer) {
        this.players.remove(exitPlayer);
        this.leaderBoard.remove(exitPlayer.getUserId());
        this.scoreBoard.remove(exitPlayer.getUserId());
    }

    public void updateHost(Player newHost) {
        this.hostId = newHost.getUserId();
        this.hostname = newHost.getUsername();
    }

    public void finishGame() {
        this.isStart = false;
        this.leaderBoard.clear();
        this.scoreBoard.clear();
        this.round = 0;
        this.curSubmitCount.set(0);

        // 게임 끝나고 isOnRoom false인 유저 제거
        List<Player> filteredPlayers = this.players.stream()
            .filter(player -> player.getIsOnRoom().equals(true))
            .toList();

        this.players.clear();
        this.players.addAll(filteredPlayers);
    }

    public void addSubmitItem(Long userId) {

        boolean alreadyHasSubmitItem = submits.stream()
            .anyMatch(player -> player.getUserId().equals(userId)); // 이미 Player가 submitItem을 가지는지 확인

        if (alreadyHasSubmitItem) { // 이미 submitItem을 가지고 있으면 추가하지 않음
            return;
        }

        this.submits.add(SubmitItem.builder().userId(userId).isSubmit(false).build());
    }

    private void readyGameState(List<Problem> problems) {
        this.problems.clear();
        this.problems.addAll(problems);
        this.isStart = true;
        this.leaderBoard.clear();
        this.scoreBoard.clear();
        for(Player player : players) {
            this.leaderBoard.put(player.getUserId(), 0);
            this.scoreBoard.put(player.getUserId(), 0);
        }
    }
}
