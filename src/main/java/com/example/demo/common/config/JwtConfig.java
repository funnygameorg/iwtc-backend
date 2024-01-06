package com.example.demo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.common.jwt.JwtService;

@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.token-validity-milli-seconds.access}")
	private Long accessTokenValidityMilliSeconds;
	@Value("${jwt.token-validity-milli-seconds.refresh}")
	private Long refreshTokenValidityMilliSeconds;

	@Bean
	public JwtService jwtService() {
		return new JwtService(secret, accessTokenValidityMilliSeconds, refreshTokenValidityMilliSeconds);
	}
}
