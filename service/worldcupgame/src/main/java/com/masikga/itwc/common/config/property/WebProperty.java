package com.masikga.itwc.common.config.property;

import com.masikga.itwc.common.config.property.exception.IncompleteBeanConfigurationException;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "origin")
public record WebProperty(
        String front
) {

    @PostConstruct
    void postConstruct() {
        if (Stream.of(front)
                .anyMatch(Objects::isNull)) {
            throw new IncompleteBeanConfigurationException(this.getClass().getSimpleName());
        }
    }
}
