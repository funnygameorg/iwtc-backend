package com.example.demo.common.config.property;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.demo.common.config.property.exception.IncompleteBeanConfigurationException;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties(prefix = "spring.redis")
public record RedisProperty(
	String host,
	int port
) {
	@PostConstruct
	void postConstruct() {
		if (Stream.of(host, port)
			.anyMatch(Objects::isNull)) {
			throw new IncompleteBeanConfigurationException(this.getClass().getSimpleName());
		}
	}
}
