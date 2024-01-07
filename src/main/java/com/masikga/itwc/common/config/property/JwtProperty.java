package com.masikga.itwc.common.config.property;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.masikga.itwc.common.config.property.exception.IncompleteBeanConfigurationException;

import jakarta.annotation.PostConstruct;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(
	String secret,
	TokenValidityMilliSeconds tokenValidityMilliSeconds

) {
	@PostConstruct
	void postConstruct() {
		if (Stream.of(secret, tokenValidityMilliSeconds, tokenValidityMilliSeconds.access,
				tokenValidityMilliSeconds.refresh)
			.anyMatch(Objects::isNull)) {
			throw new IncompleteBeanConfigurationException(this.getClass().getSimpleName());
		}
	}

	public record TokenValidityMilliSeconds(
		Long access,
		Long refresh
	) {
	}
}
