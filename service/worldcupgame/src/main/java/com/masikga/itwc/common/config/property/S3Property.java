package com.masikga.itwc.common.config.property;

import com.masikga.itwc.common.config.property.exception.IncompleteBeanConfigurationException;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "cloud.aws")
public record S3Property(
	String region,
	String endpoint,
	Credentials credentials

) {

	@PostConstruct
	void postConstruct() {
		if (Stream.of(region, endpoint, credentials, credentials.accessKey, credentials.secretKey)
			.anyMatch(Objects::isNull)) {
			throw new IncompleteBeanConfigurationException(this.getClass().getSimpleName());
		}
	}

	public record Credentials(
		String accessKey,
		String secretKey
	) {
	}

}
