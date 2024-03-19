package com.masikga.jwt.common.config.property;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {
    private String secret;
    private TokenValidityMilliSeconds tokenValidityMilliSeconds;

    @PostConstruct
    void postConstruct() {
        if (Stream.of(secret, tokenValidityMilliSeconds, tokenValidityMilliSeconds.access,
                        tokenValidityMilliSeconds.refresh)
                .anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName());
        }
    }

}