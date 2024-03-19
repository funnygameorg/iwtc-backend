package com.masikga.worldcupgame.common.config;

import com.masikga.worldcupgame.common.log.LogComponent;
import com.masikga.worldcupgame.common.log.LoggingAspect;
import com.masikga.worldcupgame.common.log.WebLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

	@Bean
	public com.masikga.worldcupgame.common.log.LoggingAspect loggingAspect() {
		return new LoggingAspect(logComponent());
	}

	@Bean
	public com.masikga.worldcupgame.common.log.WebLoggingFilter webLoggingFilter() {
		return new WebLoggingFilter(logComponent());
	}

	private com.masikga.worldcupgame.common.log.LogComponent logComponent() {
		return new LogComponent();
	}
}
