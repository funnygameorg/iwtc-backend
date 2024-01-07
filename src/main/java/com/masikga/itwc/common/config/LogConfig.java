package com.masikga.itwc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.masikga.itwc.common.log.LogComponent;
import com.masikga.itwc.common.log.LoggingAspect;
import com.masikga.itwc.common.log.WebLoggingFilter;

@Configuration
public class LogConfig {

	@Bean
	public LoggingAspect loggingAspect() {
		return new LoggingAspect(logComponent());
	}

	@Bean
	public WebLoggingFilter webLoggingFilter() {
		return new WebLoggingFilter(logComponent());
	}

	private LogComponent logComponent() {
		return new LogComponent();
	}
}
