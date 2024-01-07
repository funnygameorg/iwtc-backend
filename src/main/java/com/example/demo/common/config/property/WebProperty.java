package com.example.demo.common.config.property;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.demo.common.config.property.exception.IncompleteBeanConfigurationException;

import jakarta.annotation.PostConstruct;

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
