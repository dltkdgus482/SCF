package com.scf.user.member.application.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValues(String key, String value) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    // refresh expire 시간도 함께 저장
    @Override
    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    @Override
    public String getValue(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key); // null일 경우 null 반환
    }


    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

}
