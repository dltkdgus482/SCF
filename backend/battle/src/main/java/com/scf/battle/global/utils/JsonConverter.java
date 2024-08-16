package com.scf.battle.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonConverter() {
    }

    private static class SingletonHelper {

        private static final JsonConverter INSTANCE = new JsonConverter();
    }

    public static JsonConverter getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // 객체를 JSON 문자열로 변환
    public String toString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    // JSON 문자열을 객체로 변환
    public <T> T toObject(String jsonString, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonString, valueType);
    }
}
