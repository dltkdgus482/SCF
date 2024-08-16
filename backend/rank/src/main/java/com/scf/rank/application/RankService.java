package com.scf.rank.application;

import com.scf.rank.constant.RedisRankType;
import com.scf.rank.domain.model.UserExp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankService {

    private final RedisTemplate<String, UserExp> redisTemplate;
    private static final String KEY_PREFIX = "rank:";

    public List<UserExp> getAllTimeRankings() {
        return getRankings(RedisRankType.TOTAL.key);
    }

    public List<UserExp> getWeeklyRankings() {
        return getRankings(getWeeklyPrefix(LocalDate.now()));
    }

    public List<UserExp> getDailyRankings() {
        return getRankings(getDailyPrefix(LocalDate.now()));
    }

    public UserExp getUserRank(Long userId) {

        String key = KEY_PREFIX + userId;

        ValueOperations<String, UserExp> valueOps = redisTemplate.opsForValue();

        return valueOps.get(key);
    }

    @Transactional
    public void updateTotalRank(UserExp userExp) {

        String key = RedisRankType.TOTAL.key;

        redisTemplate.opsForHash().put(key, userExp.getUserId(), userExp);
    }

    @Transactional
    public void updateRank(UserExp userExp, String key) {

        // 기존 점수를 가져옵니다.
        UserExp updatedExp = (UserExp) redisTemplate.opsForHash().get(key, userExp.getUserId());

        if (updatedExp != null) {

            // 기존 점수가 있는 경우, exp를 더합니다.
            updatedExp.addExp(userExp.getExp());

            // 사용자 데이터를 Redis에 저장합니다.
            redisTemplate.opsForHash().put(key, userExp.getUserId(), updatedExp);

            return;
        }

        redisTemplate.opsForHash().put(key, userExp.getUserId(), userExp);
    }

    public List<UserExp> getRankings(String key) {

        Map<Object, Object> userRanks = redisTemplate.opsForHash().entries(key);

        List<UserExp> userExps = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : userRanks.entrySet()) {
            UserExp userExp = (UserExp) entry.getValue();
            userExps.add(userExp);
        }

        // exp를 기준으로 내림차순 정렬
        return userExps.stream()
            .sorted((r1, r2) -> Integer.compare(r2.getExp(), r1.getExp()))
            .collect(Collectors.toList());
    }

    public String getWeeklyPrefix(LocalDate date) {
        // 주간 랭킹 키 생성 (예: "user:weekly:2024-W31")
        String weekOfYear = String.format("W%02d", date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
        return RedisRankType.WEEKLY.key + ":" + date.getYear() + "-" + weekOfYear;
    }

    public String getDailyPrefix(LocalDate date) {
        // 일간 랭킹 키 생성 (예: "user:daily:2024-07-31")
        return RedisRankType.DAILY.key + ":" + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // 일간 랭킹 초기화
    @Scheduled(cron = "59 59 23 * * ?") // 매일 23:59:59에 실행
    public void resetDailyRankings() {
        resetRankings("rank:daily:*");
    }

    // 주간 랭킹 초기화
    @Scheduled(cron = "59 59 23 * * SUN") // 매주 일요일 23:59:59에 실행
    public void resetWeeklyRankings() {
        resetRankings("rank:weekly:*");
    }

    // 공통적인 랭킹 초기화 메서드
    private void resetRankings(String pattern) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                redisTemplate.delete(key);
            }
        }
    }
}
