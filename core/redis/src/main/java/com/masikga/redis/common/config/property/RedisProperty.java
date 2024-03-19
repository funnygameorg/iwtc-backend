package com.masikga.redis.common.config.property;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "spring.redis")
public record RedisProperty(
        String host,
        int port
) {
    @PostConstruct
    void postConstruct() {
        if (Stream.of(host, port)
                .anyMatch(Objects::isNull)) {
            throw new IllegalStateException(this.getClass().getSimpleName());
        }
    }
}
