package com.scf.user.profile.global.config;

import com.scf.user.profile.domain.dto.kafka.BattleGameResult;
import com.scf.user.profile.domain.dto.kafka.MultiGameResult;
import com.scf.user.profile.domain.dto.kafka.Solved;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private final String bootstrapServers = "kafka:9092";
    private final String groupId = "game-results-group";
    private final String solvedGroupId = "solved-problems-group";

    // BattleGameResult consumer
    @Bean
    public ConsumerFactory<String, BattleGameResult> battleGameResultConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.scf.user.profile.domain.dto.kafka.BattleGameResult");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BattleGameResult> battleGameResultKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BattleGameResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(battleGameResultConsumerFactory());
        return factory;
    }

    // game 결과 consumer
    @Bean
    public ConsumerFactory<String, MultiGameResult> rankConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
            "com.scf.user.profile.domain.dto.kafka.MultiGameResult");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MultiGameResult> multiGameResultkafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MultiGameResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rankConsumerFactory());
        return factory;
    }

    // 푼 문제 consumer
    @Bean
    public ConsumerFactory<String, Solved> solvedConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, solvedGroupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
            "com.scf.user.profile.domain.dto.kafka.Solved");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Solved> solvedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Solved> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(solvedConsumerFactory());
        return factory;
    }


}
