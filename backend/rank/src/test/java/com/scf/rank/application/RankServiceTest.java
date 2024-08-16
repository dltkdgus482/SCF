package com.scf.rank.application;

import com.scf.rank.domain.model.UserExp;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RankServiceTest {

    @Mock
    private RedisTemplate<String, UserExp> redisTemplate;

    @InjectMocks
    private RankService rankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTimeRankingsTest() {
        // Arrange
        String datePrefix = "rank:total:";
        UserExp user1 = new UserExp(1L, "User1", 100);
        UserExp user2 = new UserExp(2L, "User2", 150);

        Set<String> keys = new HashSet<>(Arrays.asList(datePrefix + user1.getUserId(), datePrefix + user2.getUserId()));
        ValueOperations<String, UserExp> valueOps = mock(ValueOperations.class);

        // Mocking RedisTemplate behavior
        when(redisTemplate.keys(datePrefix + "*")).thenReturn(keys);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(datePrefix + user1.getUserId())).thenReturn(user1);
        when(valueOps.get(datePrefix + user2.getUserId())).thenReturn(user2);

        // Act
        List<UserExp> rankings = rankService.getRankings(datePrefix);

        // Assert
        assertNotNull(rankings);
        assertEquals(2, rankings.size());
        assertEquals("User2", rankings.get(0).getName()); // User2 should be first due to higher exp
        assertEquals("User1", rankings.get(1).getName());
    }

    @Test
    void getWeeklyRankingsTest() {
        // Arrange
        String datePrefix = "rank:weekly:";
        UserExp user1 = new UserExp(1L, "User1", 100);
        UserExp user2 = new UserExp(2L, "User2", 150);

        Set<String> keys = new HashSet<>(Arrays.asList(datePrefix + user1.getUserId(), datePrefix + user2.getUserId()));
        ValueOperations<String, UserExp> valueOps = mock(ValueOperations.class);

        // Mocking RedisTemplate behavior
        when(redisTemplate.keys(datePrefix + "*")).thenReturn(keys);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(datePrefix + user1.getUserId())).thenReturn(user1);
        when(valueOps.get(datePrefix + user2.getUserId())).thenReturn(user2);

        // Act
        List<UserExp> rankings = rankService.getRankings(datePrefix);

        // Assert
        assertNotNull(rankings);
        assertEquals(2, rankings.size());
        assertEquals("User2", rankings.get(0).getName()); // User2 should be first due to higher exp
        assertEquals("User1", rankings.get(1).getName());
    }

    @Test
    void getDailyRankingsTest() {
        // Arrange
        String datePrefix = "rank:daily:";
        UserExp user1 = new UserExp(1L, "User1", 100);
        UserExp user2 = new UserExp(2L, "User2", 150);

        Set<String> keys = new HashSet<>(Arrays.asList(datePrefix + user1.getUserId(), datePrefix + user2.getUserId()));
        ValueOperations<String, UserExp> valueOps = mock(ValueOperations.class);

        // Mocking RedisTemplate behavior
        when(redisTemplate.keys(datePrefix + "*")).thenReturn(keys);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(datePrefix + user1.getUserId())).thenReturn(user1);
        when(valueOps.get(datePrefix + user2.getUserId())).thenReturn(user2);

        // Act
        List<UserExp> rankings = rankService.getRankings(datePrefix);

        // Assert
        assertNotNull(rankings);
        assertEquals(2, rankings.size());
        assertEquals("User2", rankings.get(0).getName()); // User2 should be first due to higher exp
        assertEquals("User1", rankings.get(1).getName());
    }

    @Test
    void updateRankTest() {
        // Arrange
        UserExp userExp = new UserExp(1L, "User1", 100);
        String datePrefix = "rank:daily:";
        String key = datePrefix + userExp.getUserId();
        ZSetOperations<String, UserExp> zSetOps = mock(ZSetOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOps);
        when(zSetOps.score(key, userExp)).thenReturn(50.0);

        // Act
        rankService.updateRank(userExp, datePrefix);

        // Assert
        verify(zSetOps).incrementScore(key, userExp, 100);
    }

    @Test
    void getWeeklyPrefixTest() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 7, 31);
        String expectedPrefix = "rank:weekly:2024-W31:";

        // Act
        String prefix = rankService.getWeeklyPrefix(date);

        // Assert
        assertEquals(expectedPrefix, prefix);
    }

    @Test
    void getDailyPrefixTest() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 7, 31); // Example date
        String expectedPrefix = "rank:daily:2024-07-31:";

        // Act
        String prefix = rankService.getDailyPrefix(date);

        // Assert
        assertEquals(expectedPrefix, prefix);
    }
}
