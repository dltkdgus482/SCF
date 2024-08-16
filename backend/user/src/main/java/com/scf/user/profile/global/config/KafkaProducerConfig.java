package com.scf.user.profile.global.config;

import com.scf.user.profile.domain.dto.kafka.RenewExp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, RenewExp> renewExpProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs(RenewExp.class));
    }

    @Bean
    public KafkaTemplate<String, RenewExp> renewExpKafkaTemplate() {
        return new KafkaTemplate<>(renewExpProducerFactory());
    }


    @Bean
    public Map<String, Object> producerConfigs(Class<?> valueClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        return props;
    }


}
