package com.example.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.common.config.property.JwtProperty;
import com.example.demo.common.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

	private final JwtProperty jwtProperty;

	@Bean
	public JwtService jwtService() {
		return new JwtService(
			jwtProperty.secret(),
			jwtProperty.tokenValidityMilliSeconds().access(),
			jwtProperty.tokenValidityMilliSeconds().refresh()
		);
	}
}
